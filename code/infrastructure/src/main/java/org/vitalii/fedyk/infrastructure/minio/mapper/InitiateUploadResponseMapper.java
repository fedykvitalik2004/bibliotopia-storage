package org.vitalii.fedyk.infrastructure.minio.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.InitiateUploadResponseDto;
import org.vitalii.fedyk.domain.minio.model.InitiateUploadResponse;

/**
 * Mapper interface for converting between {@link InitiateUploadResponse} domain objects and {@link
 * InitiateUploadResponseDto} data transfer objects.
 */
@Mapper(componentModel = "spring")
public interface InitiateUploadResponseMapper {
  InitiateUploadResponseDto toDto(InitiateUploadResponse initiateUploadResponse);
}
