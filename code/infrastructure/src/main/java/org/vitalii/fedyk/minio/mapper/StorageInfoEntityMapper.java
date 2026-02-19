package org.vitalii.fedyk.minio.mapper;

import org.mapstruct.Mapper;
import org.vitalii.fedyk.minio.entity.StorageInfoEntity;
import org.vitalii.fedyk.minio.model.StorageInfo;

@Mapper(componentModel = "spring")
public interface StorageInfoEntityMapper {
  StorageInfo toModel(StorageInfoEntity entity);

  StorageInfoEntity toEntity(StorageInfo model);
}
