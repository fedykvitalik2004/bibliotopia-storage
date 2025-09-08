package org.vitalii.fedyk.minio.usecase;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.minio.exception.FileStorageException;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;
import org.vitalii.fedyk.minio.repository.FileStorageRepository;

/**
 * {@inheritDoc}
 */
@Service
@AllArgsConstructor
@Slf4j
public class FileStorageUseCaseImpl implements FileStorageUseCase {

  private final FileStorageRepository repository;

  @Override
  public FileStorageResult uploadFile(StorageLocation location, FileUpload file) {
    log.info("Starting file upload for object '{}' in bucket '{}'",
            location.objectKey(), location.bucketName());

    validateServiceAvailability();
    validateFile(file);

    try {
      final FileStorageResult result = repository.store(location, file);
      log.info("Successfully uploaded file '{}' to bucket '{}'",
              location.objectKey(), location.bucketName());
      return result;
    } catch (Exception e) {
      log.error("Failed to upload file '{}' to bucket '{}': {}",
              location.objectKey(), location.bucketName(), e.getMessage(), e);
      throw new FileStorageException("File upload failed", e);
    }
  }

  @Override
  public String getFileAccessUrl(StorageLocation location) {
    log.info("Generating access URL for object '{}' in bucket '{}'",
            location.objectKey(), location.bucketName());

    try {
      String url = repository.generateAccessUrl(location);
      log.info("Successfully generated access URL for object '{}' in bucket '{}'",
              location.objectKey(), location.bucketName());
      return url;
    } catch (Exception e) {
      log.error("Failed to generate URL for object '{}' in bucket '{}': {}",
              location.objectKey(), location.bucketName(), e.getMessage(), e);
      throw new FileStorageException("Failed to generate access URL", e);
    }
  }

  private void validateServiceAvailability() {
    if (!repository.isHealthy()) {
      throw new FileStorageException("File storage service is not available");
    }
  }

  private void validateFile(FileUpload file) {
    if (file == null) {
      throw new IllegalArgumentException("File cannot be null");
    }
    if (file.content() == null || file.content().length == 0 ) {
      throw new IllegalArgumentException("File content cannot be empty");
    }
  }
}