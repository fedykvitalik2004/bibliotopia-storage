package org.vitalii.fedyk.generation.usecase;

/** Use case for generating barcode images and processing them. */
public interface GenerationProcessingUseCase {
  String generateBarcodeAndReturnUrl(String isbn);
}
