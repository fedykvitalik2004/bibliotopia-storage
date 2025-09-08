package org.vitalii.fedyk.minio.repository;

import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;

/**
 * Repository interface for interacting with MinIO object storage.
 */
public interface FileStorageRepository {
  FileStorageResult store(StorageLocation location, FileUpload file);

  String generateAccessUrl(StorageLocation location);

  boolean isHealthy();
}