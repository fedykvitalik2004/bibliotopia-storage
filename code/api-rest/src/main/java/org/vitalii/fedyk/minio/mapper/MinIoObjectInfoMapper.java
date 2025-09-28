package org.vitalii.fedyk.minio.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.FileUploadResponse;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;

/** Mapper interface for converting between MinIoResponse and FileUploadResponse (OpenAPI model). */
@Mapper(componentModel = "spring")
public interface MinIoObjectInfoMapper {
  FileUploadResponse toFileUploadResponse(MinIoObjectInfo response);
}
