package org.vitalii.fedyk.minio.mapper;

import org.mapstruct.Mapper;
import org.vitalii.fedyk.minio.entity.MinIoObjectInfoEntity;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

@Mapper(componentModel = "spring")
public interface MinIoObjectInfoEntityMapper {
  MinIoObjectInfo toModel(MinIoObjectInfoEntity entity);

  MinIoObjectInfoEntity toEntity(MinIoObjectInfo entity);
}
