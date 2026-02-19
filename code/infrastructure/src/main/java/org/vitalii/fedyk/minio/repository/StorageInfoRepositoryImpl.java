package org.vitalii.fedyk.minio.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.entity.StorageInfoEntity;
import org.vitalii.fedyk.minio.mapper.StorageInfoEntityMapper;
import org.vitalii.fedyk.minio.model.StorageInfo;

/** {@inheritDoc} */
@Repository
@AllArgsConstructor
public class StorageInfoRepositoryImpl implements StorageInfoRepository {
  private final StorageInfoJpaAdapter adapter;

  private final StorageInfoEntityMapper mapper;

  @Override
  public StorageInfo save(final StorageInfo storageInfo) {
    final StorageInfoEntity entity = this.mapper.toEntity(storageInfo);
    final StorageInfoEntity persisted = this.adapter.save(entity);
    return mapper.toModel(persisted);
  }

  @Override
  public Optional<StorageInfo> findById(final UUID id) {
    return this.adapter.findById(id).map(this.mapper::toModel);
  }

  @Override
  public Optional<StorageInfo> findByBucketNameAndObjectName(
      final String bucketName, final String objectName) {
    return this.adapter
        .findByBucketNameAndObjectName(bucketName, objectName)
        .map(this.mapper::toModel);
  }
}
