package org.vitalii.fedyk.minio.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.entity.MinIoObjectInfoEntity;

/**
 * Repository interface for performing CRUD operations on {@link MinIoObjectInfoEntity} entities.
 */
@Repository
public interface MinIoObjectInfoJpaAdapter extends JpaRepository<MinIoObjectInfoEntity, UUID> {
  Optional<MinIoObjectInfoEntity> findByBucketNameAndObjectName(
      String bucketName, String objectName);
}
