package org.vitalii.fedyk.infrastructure.minio.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.StorageInfoDto;
import org.vitalii.fedyk.domain.minio.model.StorageInfo;

/** Mapper interface for converting between StorageInfo and StorageInfoDto. */
@Mapper(componentModel = "spring")
public interface StorageInfoDtoMapper {
  StorageInfoDto toDto(StorageInfo model);
}
