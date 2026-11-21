package org.vitalii.fedyk.infrastructure.minio.mapper;

import org.mapstruct.Mapper;
import org.vitalii.fedyk.domain.minio.model.StorageInfo;
import org.vitalii.fedyk.infrastructure.minio.entity.StorageInfoEntity;

@Mapper(componentModel = "spring")
public interface StorageInfoEntityMapper {
  StorageInfo toModel(StorageInfoEntity entity);

  StorageInfoEntity toEntity(StorageInfo model);
}
