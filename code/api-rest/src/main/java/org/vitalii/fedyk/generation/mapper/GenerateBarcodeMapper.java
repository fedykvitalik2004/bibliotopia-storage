package org.vitalii.fedyk.generation.mapper;

import org.mapstruct.Mapper;
import org.openapitools.model.GenerateBarcode200ResponseDto;

/** Mapper for converting internal barcode data into API response DTOs. */
@Mapper(componentModel = "spring")
public interface GenerateBarcodeMapper {
  GenerateBarcode200ResponseDto toDto(String url);
}
