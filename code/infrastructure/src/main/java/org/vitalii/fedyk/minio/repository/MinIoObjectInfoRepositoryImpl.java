package org.vitalii.fedyk.minio.repository;

import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.entity.MinIoObjectInfoEntity;
import org.vitalii.fedyk.minio.mapper.MinIoObjectInfoEntityMapper;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

/**
 * {@inheritDoc}
 */
@Repository
@AllArgsConstructor
public class MinIoObjectInfoRepositoryImpl implements MinIoObjectInfoRepository {
  private MinIoObjectInfoJpaAdapter minIoObjectInfoJpaAdapter;
  private MinIoObjectInfoEntityMapper mapper;

  @Override
  public MinIoObjectInfo save(final MinIoObjectInfo minIoObjectInfo) {
    final MinIoObjectInfoEntity entity = mapper.toEntity(minIoObjectInfo);
    final MinIoObjectInfoEntity persisted = minIoObjectInfoJpaAdapter.save(entity);
    return mapper.toModel(persisted);
  }

  @Override
  public Optional<MinIoObjectInfo> findByBucketNameAndObjectName(final String bucketName, final String objectName) {
    return minIoObjectInfoJpaAdapter.findByBucketNameAndObjectName(bucketName, objectName)
            .map(mapper::toModel);
  }

  @Override
  public Optional<MinIoObjectInfo> findById(final UUID id) {
    return minIoObjectInfoJpaAdapter.findById(id)
            .map(minIoObjectInfoEntity -> mapper.toModel(minIoObjectInfoEntity));
  }
}
