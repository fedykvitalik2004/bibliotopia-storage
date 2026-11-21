package org.vitalii.fedyk.domain.minio.usecase;

import java.io.InputStream;
import org.vitalii.fedyk.domain.minio.model.CompleteRequest;
import org.vitalii.fedyk.domain.minio.model.FileStorageResult;
import org.vitalii.fedyk.domain.minio.model.FileUpload;
import org.vitalii.fedyk.domain.minio.model.StorageLocation;

/** A service interface for managing file storage operations. */
public interface FileStorageService {
  FileStorageResult uploadFile(String bucketName, FileUpload file);

  String getFileAccessUrl(StorageLocation location);

  String createMultiPartUpload(StorageLocation storageLocation);

  String uploadChunk(
      String bucketName,
      InputStream chunk,
      int chunkSize,
      String uploadId,
      Integer chunkNumber,
      String fileName);

  String completeUpload(String appName, CompleteRequest completeRequest);

  void ensureBucketExists(String bucketName);

  boolean existsBucket(String bucketName);
}
