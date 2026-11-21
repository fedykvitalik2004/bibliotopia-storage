package org.vitalii.fedyk.domain.minio.repository;

import java.io.InputStream;
import org.vitalii.fedyk.domain.minio.model.CompleteRequest;
import org.vitalii.fedyk.domain.minio.model.FileStorageResult;
import org.vitalii.fedyk.domain.minio.model.FileUpload;
import org.vitalii.fedyk.domain.minio.model.StorageLocation;

/** Repository interface for interacting with MinIO object storage. */
public interface FileStorageRepository {
  FileStorageResult store(String bucketName, FileUpload file);

  String generateAccessUrl(StorageLocation location);

  InputStream getObjectStream(String bucketName, String objectName);

  String createMultiPartUpload(String filename, String bucket);

  String uploadChunk(
      String bucketName,
      InputStream chunk,
      int chunkSize,
      String uploadId,
      Integer chunkNumber,
      String fileName);

  void completeUpload(String bucket, CompleteRequest completeRequest);

  void createBucket(String bucketName);

  boolean existsBucket(String bucketName);
}
