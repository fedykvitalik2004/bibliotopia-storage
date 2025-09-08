package org.vitalii.fedyk.minio.repository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.exception.FileStorageException;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
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
@Repository
@Slf4j
public class MinioFileStorageRepository implements FileStorageRepository {
  private final S3Client s3Client;
  private final S3Presigner s3Presigner;
  private final String healthCheckUrl;
  private final HttpClient httpClient;

  /**
   * Constructs a new {@code MinioFileStorageRepository} instance.
   * This constructor initializes the AWS S3 client and presigner, the MinIO
   * health check URL, and an HTTP client for health checks. The dependencies
   * are injected by the Spring framework.
   *
   * @param s3Client       The S3 client for performing low-level S3-compatible operations.
   * @param s3Presigner    The S3 presigner used to generate secure pre-signed URLs for file access.
   * @param healthCheckUrl The URL for the MinIO health check endpoint, provided via Spring's {@code @Value}.
   */
  @Autowired
  public MinioFileStorageRepository(S3Client s3Client,
                                    S3Presigner s3Presigner,
                                    @Value("#{ '${minio.endpoint}' + '/minio/health/live' }") String healthCheckUrl) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
    this.healthCheckUrl = healthCheckUrl;
    this.httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();
  }

  @Override
  public FileStorageResult store(StorageLocation location, FileUpload file) {
    try {
      ensureBucketExists(location.bucketName());

      final PutObjectResponse response = s3Client.putObject(
              PutObjectRequest.builder()
                      .bucket(location.bucketName())
                      .key(location.objectKey())
                      .contentType(file.contentType())
                      .contentLength(file.size())
                      .build(),
              RequestBody.fromInputStream(
                      new ByteArrayInputStream(file.content()),
                      file.size())
      );

      if (!response.sdkHttpResponse().isSuccessful()) {
        throw new FileStorageException("Upload failed with status: "
                                       + response.sdkHttpResponse().statusCode());
      }

      final String accessUrl = generateAccessUrl(location);

      return new FileStorageResult(location, accessUrl, file.size(), file.contentType());

    } catch (S3Exception e) {
      log.error("S3 error during file storage: {}", e.getMessage(), e);
      throw new FileStorageException("S3 storage failed: " + e.awsErrorDetails().errorMessage(), e);
    } catch (Exception e) {
      log.error("Unexpected error during file storage: {}", e.getMessage(), e);
      throw new FileStorageException("File storage failed", e);
    }
  }

  @Override
  public String generateAccessUrl(StorageLocation location) {
    try {
      GetObjectPresignRequest request = GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofHours(1))
              .getObjectRequest(builder -> builder
                      .bucket(location.bucketName())
                      .key(location.objectKey()))
              .build();

      return s3Presigner.presignGetObject(request).url().toString();

    } catch (S3Exception e) {
      log.error("Failed to generate presigned URL: {}", e.getMessage(), e);
      throw new FileStorageException("Failed to generate access URL: "
                                     + e.awsErrorDetails().errorMessage(), e);
    }
  }

  @Override
  public boolean isHealthy() {
    try {
      HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(healthCheckUrl))
              .timeout(Duration.ofSeconds(4))
              .GET()
              .build();

      HttpResponse<?> response = httpClient.send(request,
              HttpResponse.BodyHandlers.discarding());

      boolean isHealthy = response.statusCode() == 200;
      if (!isHealthy) {
        log.warn("MinIO health check failed with status: {}", response.statusCode());
      }
      return isHealthy;

    } catch (IOException | InterruptedException e) {
      log.error("MinIO health check failed: {}", e.getMessage());
      return false;
    }
  }

  private void ensureBucketExists(String bucketName) {
    try {
      s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
    } catch (NoSuchBucketException e) {
      log.info("Creating bucket: {}", bucketName);
      try {
        s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
      } catch (S3Exception s3Exception) {
        throw new FileStorageException("Failed to create bucket: " + bucketName, s3Exception);
      }
    } catch (S3Exception e) {
      throw new FileStorageException("Failed to check bucket existence: " + bucketName, e);
    }
  }
}
