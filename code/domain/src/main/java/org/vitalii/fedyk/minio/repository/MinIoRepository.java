package org.vitalii.fedyk.minio.repository;

import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.MinIoUploadResultVO;

/**
 * Repository interface for interacting with MinIO object storage.
 */
public interface MinIoRepository {
  MinIoUploadResultVO uploadFile(String bucketName, String objectKey, MultipartFile file);

  String generatePresignedUrl(String bucketName, String objectKey);

  void ensureBucketExists(String bucketName);

  boolean isServiceHealthy();
}