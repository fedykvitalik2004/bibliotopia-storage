package org.vitalii.fedyk.generation.controller;

import lombok.RequiredArgsConstructor;
import org.openapitools.api.GenerationApi;
import org.openapitools.model.GenerateBarcode200Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.vitalii.fedyk.generation.mapper.GenerateBarcodeMapper;
import org.vitalii.fedyk.generation.usecase.GenerationProcessingUseCase;

/** REST controller responsible for handling generation requests. */
@RestController
@RequiredArgsConstructor
public class GenerationController implements GenerationApi {
  private final GenerationProcessingUseCase generationProcessingUseCase;
  private final GenerateBarcodeMapper generateBarcodeMapper;

  @Override
  public ResponseEntity<GenerateBarcode200Response> generateBarcode(String isbn) {
    final String url = generationProcessingUseCase.generateBarcodeAndReturnUrl(isbn);
    return ResponseEntity.ok(generateBarcodeMapper.toDto(url));
  }
}
