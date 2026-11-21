package org.vitalii.fedyk.domain.minio.repository;

import java.util.Optional;
import java.util.UUID;
import org.vitalii.fedyk.domain.minio.model.StorageInfo;

/** Repository interface for managing {@link StorageInfo} domain objects. */
public interface StorageInfoRepository {
  StorageInfo save(StorageInfo minIoObjectInfo);

  Optional<StorageInfo> findById(UUID id);

  Optional<StorageInfo> findByBucketNameAndObjectName(String bucketName, String objectName);
}
