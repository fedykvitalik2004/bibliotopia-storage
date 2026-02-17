package org.vitalii.fedyk.minio.repository;

import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.exception.FileStorageException;
import org.vitalii.fedyk.minio.model.CompleteRequest;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CompleteMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.CompletedPart;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.CreateMultipartUploadRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.UploadPartRequest;
import software.amazon.awssdk.services.s3.model.UploadPartResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

/** {@inheritDoc} */
@Repository
@Slf4j
public class MinioFileStorageAdapter implements FileStorageRepository {
  private final S3Client s3Client;

  private final S3Presigner s3Presigner;

  /**
   * Constructs a new {@code MinioFileStorageRepository} instance. This constructor initializes the
   * AWS S3 client and presigner.
   *
   * @param s3Client The S3 client for performing low-level S3-compatible operations.
   * @param s3Presigner The S3 presigner used to generate secure pre-signed URLs for file access.
   */
  @Autowired
  public MinioFileStorageAdapter(final S3Client s3Client, final S3Presigner s3Presigner) {
    this.s3Client = s3Client;
    this.s3Presigner = s3Presigner;
  }

  @Override
  public FileStorageResult store(final String bucketName, final FileUpload file) {
    try {
      this.s3Client.putObject(
          PutObjectRequest.builder()
              .bucket(bucketName)
              .key(file.fileName())
              .contentType(file.contentType())
              .contentLength(file.size())
              .build(),
          RequestBody.fromInputStream(file.content(), file.size()));

      return new FileStorageResult(
          StorageLocation.builder().bucketName(bucketName).objectKey(file.fileName()).build(),
          null,
          file.size(),
          file.contentType());
    } catch (final Exception exception) {
      log.error("Unexpected error during file uploading", exception);
      throw new FileStorageException("exception.files_storage.upload_failed", null, exception);
    }
  }

  @Override
  public String generateAccessUrl(final StorageLocation location) {
    try {
      final GetObjectPresignRequest request =
          GetObjectPresignRequest.builder()
              .signatureDuration(Duration.ofDays(7))
              .getObjectRequest(
                  builder -> builder.bucket(location.bucketName()).key(location.objectKey()))
              .build();

      return this.s3Presigner.presignGetObject(request).url().toString();
    } catch (final Exception e) {
      log.error("Failed to generate an access URL", e);
      throw new FileStorageException(
          "exception.files_storage.access_url_generation_failed", null, e);
    }
  }

  @Override
  public InputStream getObjectStream(final String bucketName, final String fileName) {
    try {
      return s3Client.getObject(
          GetObjectRequest.builder().bucket(bucketName).key(fileName).build());
    } catch (final Exception exception) {
      log.error("Failed to retrieve file '{}' from bucket '{}'", fileName, bucketName, exception);
      throw new FileStorageException(
          "exception.files_storage.retrieve_failed",
          new Object[] {fileName, bucketName},
          exception);
    }
  }

  @Override
  public String createMultiPartUpload(final String filename, final String bucket) {
    try {
      final CreateMultipartUploadRequest createMultipartUploadRequest =
          CreateMultipartUploadRequest.builder().bucket(bucket).key(filename).build();
      return this.s3Client.createMultipartUpload(createMultipartUploadRequest).uploadId();
    } catch (final Exception exception) {
      log.error(
          "Failed to create multipart upload for file '{}' in bucket '{}'",
          filename,
          bucket,
          exception);
      throw new FileStorageException(
          "exception.files_storage.create_multipart_failed",
          new Object[] {filename, bucket},
          exception);
    }
  }

  @Override
  public String uploadChunk(
      final String bucketName,
      final InputStream chunk,
      final int chunkSize,
      final String uploadId,
      final Integer chunkNumber,
      final String fileName) {
    try {
      final UploadPartRequest uploadPartRequest =
          UploadPartRequest.builder()
              .bucket(bucketName)
              .key(fileName)
              .uploadId(uploadId)
              .partNumber(chunkNumber)
              .build();

      final UploadPartResponse response =
          this.s3Client.uploadPart(
              uploadPartRequest, RequestBody.fromInputStream(chunk, chunkSize));
      return response.eTag();
    } catch (final Exception exception) {
      log.error(
          "Failed to upload part {} for file '{}' in bucket '{}'",
          chunkNumber,
          fileName,
          bucketName,
          exception);
      throw new FileStorageException(
          "exception.files_storage.upload_part_failed",
          new Object[] {chunkNumber, fileName, bucketName},
          exception);
    }
  }

  @Override
  public void completeUpload(final String bucket, final CompleteRequest completeRequest) {
    try {
      final List<CompletedPart> completedParts =
          completeRequest.parts().stream()
              .map(
                  part ->
                      CompletedPart.builder()
                          .partNumber(part.partNumber())
                          .eTag(part.etag())
                          .build())
              .toList();

      final CompleteMultipartUploadRequest completeMultipartUploadRequest =
          CompleteMultipartUploadRequest.builder()
              .bucket(bucket)
              .key(completeRequest.fileName())
              .uploadId(completeRequest.uploadId())
              .multipartUpload(upload -> upload.parts(completedParts).build())
              .build();

      this.s3Client.completeMultipartUpload(completeMultipartUploadRequest);
    } catch (final Exception exception) {
      log.error(
          "Failed to complete multipart upload for file '{}' in bucket '{}' (uploadId: {})",
          completeRequest.fileName(),
          bucket,
          completeRequest.uploadId(),
          exception);
      throw new FileStorageException(
          "exception.files_storage.complete_multipart_failed",
          new Object[] {completeRequest.fileName(), bucket, completeRequest.uploadId()},
          exception);
    }
  }

  @Override
  public void createBucket(final String bucketName) {
    try {
      final CreateBucketRequest request = CreateBucketRequest.builder().bucket(bucketName).build();

      this.s3Client.createBucket(request);
    } catch (final Exception e) {
      log.error("Failed to create a bucket {}", bucketName);
      throw new FileStorageException(
          "exception.files_storage.bucket_creation_failed", new Object[] {bucketName}, e);
    }
  }

  @Override
  public boolean existsBucket(final String bucketName) {
    try {
      this.s3Client.headBucket(builder -> builder.bucket(bucketName));
      return true;
    } catch (final Exception e) {
      return false;
    }
  }
}
