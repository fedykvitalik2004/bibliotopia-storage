package org.vitalii.fedyk.minio.usecase;

import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

/**
 * Use case interface for interacting with MinIO (or S3-compatible) storage.
 * <p>
 * This interface defines the contract for uploading files to a specified
 * bucket and object key, and returning metadata about the stored file.
 * </p>
 */
public interface MinIoInteractionUseCase {
  MinIoObjectInfo uploadFile(String bucketName, String objectKey, MultipartFile file);

  String getUrl(String bucketName, String objectKey);
}
