package org.vitalii.fedyk.minio.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.CompleteRequestDto;
import org.vitalii.fedyk.minio.model.CompleteRequest;

/** Mapper interface for converting between MinIoResponse and FileUploadResponse (OpenAPI model). */
@Mapper(componentModel = "spring")
public interface CompleteRequestMapper {
  CompleteRequest toCompleteRequest(CompleteRequestDto completeRequestDto);
}
