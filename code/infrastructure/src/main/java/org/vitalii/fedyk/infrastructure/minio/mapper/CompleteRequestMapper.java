package org.vitalii.fedyk.infrastructure.minio.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.CompleteRequestDto;
import org.vitalii.fedyk.domain.minio.model.CompleteRequest;

/** Mapper interface for converting between MinIoResponse and FileUploadResponse (OpenAPI model). */
@Mapper(componentModel = "spring")
public interface CompleteRequestMapper {
  CompleteRequest toCompleteRequest(CompleteRequestDto completeRequestDto);
}
