package org.vitalii.fedyk.minio.repository;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.entity.StorageInfoEntity;

/** Repository interface for performing CRUD operations on {@link StorageInfoEntity} entities. */
@Repository
public interface StorageInfoJpaAdapter extends JpaRepository<StorageInfoEntity, UUID> {
  Optional<StorageInfoEntity> findByBucketNameAndObjectName(String bucketName, String objectName);
}
