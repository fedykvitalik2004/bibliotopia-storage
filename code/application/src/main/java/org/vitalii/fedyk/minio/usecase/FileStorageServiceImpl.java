package org.vitalii.fedyk.minio.usecase;

import java.io.InputStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.minio.model.CompleteRequest;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;
import org.vitalii.fedyk.minio.repository.FileStorageRepository;

/** {@inheritDoc} */
@Service
@AllArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

  private final FileStorageRepository fileStorageRepository;

  @Override
  public FileStorageResult uploadFile(final String bucketName, final FileUpload fileUpload) {
    ensureBucketExists(bucketName);

    final FileStorageResult result = this.fileStorageRepository.store(bucketName, fileUpload);
    log.info("Successfully uploaded file '{}' to bucket '{}'", fileUpload.fileName(), bucketName);

    final String accessUrl =
        this.getFileAccessUrl(
            StorageLocation.builder()
                .bucketName(bucketName)
                .objectKey(fileUpload.fileName())
                .build());
    return result.toBuilder().accessUrl(accessUrl).build();
  }

  @Override
  public String getFileAccessUrl(final StorageLocation location) {
    final String accessUrl = this.fileStorageRepository.generateAccessUrl(location);
    log.info(
        "Successfully generated access URL for object '{}' in bucket '{}'",
        location.objectKey(),
        location.bucketName());
    return accessUrl;
  }

  @Override
  public String createMultiPartUpload(final StorageLocation storageLocation) {
    this.ensureBucketExists(storageLocation.bucketName());
    return this.fileStorageRepository.createMultiPartUpload(
        storageLocation.objectKey(), storageLocation.bucketName());
  }

  @Override
  public String uploadChunk(
      final String appName,
      final InputStream chunk,
      final int chunkSize,
      final String uploadId,
      final Integer chunkNumber,
      final String fileName) {
    return this.fileStorageRepository.uploadChunk(
        appName, chunk, chunkSize, uploadId, chunkNumber, fileName);
  }

  @Override
  public String completeUpload(final String appName, final CompleteRequest completeRequest) {
    this.fileStorageRepository.completeUpload(appName, completeRequest);

    final StorageLocation storageLocation =
        StorageLocation.builder().bucketName(appName).objectKey(completeRequest.fileName()).build();
    log.info("File '{}' uploaded successfully to bucket '{}'", completeRequest.fileName(), appName);
    return this.getFileAccessUrl(storageLocation);
  }

  @Override
  public void ensureBucketExists(final String bucketName) {
    if (!this.existsBucket(bucketName)) {
      this.fileStorageRepository.createBucket(bucketName);
    }
  }

  @Override
  public boolean existsBucket(final String bucketName) {
    return this.fileStorageRepository.existsBucket(bucketName);
  }
}
