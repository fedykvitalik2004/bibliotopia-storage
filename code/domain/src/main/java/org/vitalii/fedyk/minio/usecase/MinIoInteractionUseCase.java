package org.vitalii.fedyk.minio.usecase;

import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import org.vitalii.fedyk.minio.model.MinIoUploadResultVO;

/**
 * Use case interface for interacting with MinIO (or S3-compatible) storage.
 * <p>
 * This interface defines the contract for uploading files to a specified
 * bucket and object key.
 * </p>
 */
public interface MinIoInteractionUseCase {
  MinIoUploadResultVO uploadFile(String bucketName, String objectKey, MultipartFile file);

  String getUrl(String bucketName, String objectKey);
}
