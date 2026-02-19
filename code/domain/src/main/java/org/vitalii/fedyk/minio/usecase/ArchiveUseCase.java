package org.vitalii.fedyk.minio.usecase;

import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/** Use case for creating an archive. */
public interface ArchiveUseCase {
  void streamFilesFromBucket(
      String bucketName, List<UUID> storageInfoIds, OutputStream outputStream);
}
