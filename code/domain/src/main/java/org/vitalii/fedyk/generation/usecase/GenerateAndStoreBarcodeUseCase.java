package org.vitalii.fedyk.generation.usecase;

/** Use case for generating barcode images and processing them. */
public interface GenerateAndStoreBarcodeUseCase {
  String generateBarcodeAndReturnUrl(String isbn);
}
