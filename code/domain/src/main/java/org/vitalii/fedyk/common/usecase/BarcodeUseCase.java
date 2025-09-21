package org.vitalii.fedyk.common.usecase;

import org.vitalii.fedyk.FileInfo;

/**
 * Use case for generating barcode images from ISBN codes.
 */
public interface BarcodeUseCase {
  FileInfo generateImageBarcode(String isbn);
}
