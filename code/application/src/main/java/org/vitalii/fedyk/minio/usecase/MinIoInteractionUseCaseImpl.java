package org.vitalii.fedyk.minio.usecase;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.exception.MinIoException;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/**
 * {@inheritDoc}
 */
@Service
@Slf4j
public class MinIoInteractionUseCaseImpl implements MinIoInteractionUseCase {
  private final S3Client s3Client;

  private final S3Presigner s3Presigner;

  @Value("#{ '${minio.endpoint}' + '/minio/health/live' }")
  private String minioHealthUrl;

  @Autowired
  public MinIoInteractionUseCaseImpl(final S3Client s3Client, final S3Presigner s3Presigner) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
  }

  @Override
  public MinIoObjectInfo uploadFile(final String bucketName,
                                    final String objectKey,
                                    final MultipartFile file) {
    try {
      validateMinioAvailability();
      ensureBucketExists(bucketName);

      final PutObjectResponse response = uploadToMinio(bucketName, objectKey, file);
      validateUploadResponse(response);
      final String url = getUrl(bucketName, objectKey);
      return MinIoObjectInfo.builder()
              .bucketName(bucketName)
              .objectName(objectKey)
              .url(url)
              .build();
    } catch (IOException e) {
      log.error("IO error during file upload for object '{}' in bucket '{}': {}", objectKey, bucketName, e.getMessage(), e);
      throw new MinIoException("Failed to upload file due to an IO error", e);
    } catch (S3Exception e) {
      final String errorMessage = "S3 error during file upload for object '%s' in bucket '%s': %s"
              .formatted(objectKey, bucketName, e.awsErrorDetails().errorMessage());
      log.error(errorMessage, e);
      throw new MinIoException(errorMessage, e);
    } catch (MinIoException e) {
      throw e;
    } catch (Exception e) {
      log.error("An unexpected error occurred during file upload for object '{}' in bucket '{}'", objectKey, bucketName, e);
      throw new MinIoException("An unexpected error occurred during file upload", e);
    }
  }

  @Override
  public String getUrl(final String bucketName, final String objectKey) {
    try {
      final GetObjectPresignRequest request = GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofHours(1))
              .getObjectRequest(builder -> builder
                      .bucket(bucketName)
                      .key(objectKey)
              )
              .build();

      return s3Presigner.presignGetObject(request)
              .url()
              .toString();

    } catch (S3Exception e) {
      log.error("Failed to generate presigned URL for object '{}' in bucket '{}': {}",
              objectKey, bucketName, e.awsErrorDetails().errorMessage(), e);
      throw new MinIoException("Failed to generate presigned URL: " + e.awsErrorDetails().errorMessage(), e);
    } catch (Exception e) {
      log.error("Unexpected error generating presigned URL for object '{}' in bucket '{}'", objectKey, bucketName, e);
      throw new MinIoException("Failed to generate presigned URL", e);
    }
  }

  private void ensureBucketExists(final String bucketName) {
    try {
      final HeadBucketRequest request = HeadBucketRequest.builder().bucket(bucketName).build();
      s3Client.headBucket(request);
      log.info("Bucket '{}' already exists", bucketName);

    } catch (NoSuchBucketException ex) {
      log.info("Bucket '{}' does not exist. Attempting to create it", bucketName);
      try {
        s3Client.createBucket(b -> b.bucket(bucketName));
        log.info("Successfully created bucket '{}'.", bucketName);
      } catch (S3Exception s3Exception) {
        final String errorMessage = "Failed to create bucket '%s'. S3 Error: %s"
                .formatted(bucketName, s3Exception.awsErrorDetails().errorMessage());
        log.error(errorMessage, s3Exception);
        throw new MinIoException(errorMessage, s3Exception);
      }
    } catch (S3Exception ex) {
      final String errorMessage = "S3 error while checking for bucket '%s':  %s"
              .formatted(bucketName, ex.awsErrorDetails().errorMessage());
      log.error(errorMessage, ex);
      throw new MinIoException(errorMessage, ex);
    } catch (Exception ex) {
      final String errorMessage = "An unexpected error occurred while managing bucket '%s'".formatted(bucketName);
      log.error(errorMessage, ex);
      throw new MinIoException(errorMessage, ex);
    }
  }

  private PutObjectResponse uploadToMinio(final String bucketName,
                                          final String objectKey,
                                          final MultipartFile file) throws IOException {
    try {
      final PutObjectResponse response = s3Client.putObject(
              PutObjectRequest.builder()
                      .bucket(bucketName)
                      .key(objectKey)
                      .contentType(file.getContentType())
                      .contentLength(file.getSize())
                      .build(),
              RequestBody.fromInputStream(file.getInputStream(), file.getSize())
      );
      log.info("File '{}' successfully saved to bucket '{}'", objectKey, bucketName);
      log.debug("PutObjectResponse: {}", response);

      return response;
    } catch (S3Exception e) {
      log.error("S3 error uploading object '{}' to bucket '{}': {}",
              objectKey, bucketName, e.awsErrorDetails().errorMessage(), e);
      throw new MinIoException("S3 upload of '%s' into '%s' failed: %s".formatted(objectKey, bucketName, e.awsErrorDetails().errorMessage()), e);
    }
  }

  private void validateUploadResponse(final PutObjectResponse response) {
    if (!response.sdkHttpResponse().isSuccessful()) {
      final String errorMessage = response.sdkHttpResponse()
              .statusText()
              .orElse("Unknown status text");
      final int statusCode = response.sdkHttpResponse().statusCode();
      log.error("Upload validation failed with status {}: {}", statusCode, errorMessage);
      throw new MinIoException("File upload failed with status " + statusCode + ": " + errorMessage);
    }
  }

  private void validateMinioAvailability() {
    if (!isMinioAlive()) {
      throw new MinIoException("MinIO service is not available at " + minioHealthUrl);
    }
  }

  /**
   * Checks if MinIO service is alive by calling its health endpoint.
   *
   * @return true if MinIO is responsive, false otherwise
   */
  private boolean isMinioAlive() {
    try {
      final HttpClient client = HttpClient.newBuilder()
              .connectTimeout(Duration.ofSeconds(3))
              .build();

      final HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(minioHealthUrl))
              .timeout(Duration.ofSeconds(4))
              .GET()
              .build();

      final HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());
      final boolean isHealthy = response.statusCode() == 200;

      if (!isHealthy) {
        log.warn("MinIO health check failed with status code: {}", response.statusCode());
      }

      return isHealthy;

    } catch (IOException e) {
      log.error("MinIO health check failed: unable to connect to '{}'", minioHealthUrl, e);
      return false;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      log.error("MinIO health check was interrupted", e);
      return false;
    } catch (Exception e) {
      log.error("Unexpected error occurred during MinIO health check", e);
      return false;
    }
  }
}