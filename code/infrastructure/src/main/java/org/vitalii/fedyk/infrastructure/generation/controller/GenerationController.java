package org.vitalii.fedyk.infrastructure.generation.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.GenerationApi;
import org.openapitools.model.GenerateBarcode200ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.domain.generation.usecase.GenerateAndStoreBarcodeUseCase;
import org.vitalii.fedyk.infrastructure.generation.mapper.GenerateBarcodeMapper;

/** REST controller responsible for handling generation requests. */
@RestController
@RequiredArgsConstructor
public class GenerationController implements GenerationApi {
  private final GenerateAndStoreBarcodeUseCase generateAndStoreBarcodeUseCase;

  private final GenerateBarcodeMapper generateBarcodeMapper;

  @Override
  public ResponseEntity<GenerateBarcode200ResponseDto> generateBarcode(final String isbn) {
    final String url = this.generateAndStoreBarcodeUseCase.generateBarcodeAndReturnUrl(isbn);
    return ResponseEntity.ok(this.generateBarcodeMapper.toDto(url));
  }
}
