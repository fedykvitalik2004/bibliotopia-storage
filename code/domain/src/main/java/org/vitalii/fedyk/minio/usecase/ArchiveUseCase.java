package org.vitalii.fedyk.minio.usecase;

import java.io.OutputStream;

/**
 * Use case for creating an archive.
 */
public interface ArchiveUseCase {
  void streamBucketAsStream(String bucketName, OutputStream outputStream);
}
