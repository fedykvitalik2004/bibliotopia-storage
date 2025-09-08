package org.vitalii.fedyk.minio.usecase;

import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;

/**
 * A service interface for managing file storage operations.
 */
public interface FileStorageUseCase {
  FileStorageResult uploadFile(StorageLocation location, FileUpload file);

  String getFileAccessUrl(StorageLocation location);
}