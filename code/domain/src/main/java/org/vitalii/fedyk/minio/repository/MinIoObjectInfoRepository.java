package org.vitalii.fedyk.minio.repository;

import java.util.Optional;
import java.util.UUID;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

/** Repository interface for managing {@link MinIoObjectInfo} domain objects. */
public interface MinIoObjectInfoRepository {
  MinIoObjectInfo save(MinIoObjectInfo minIoObjectInfo);

  Optional<MinIoObjectInfo> findByBucketNameAndObjectName(String bucketName, String objectName);

  Optional<MinIoObjectInfo> findById(UUID id);
}
