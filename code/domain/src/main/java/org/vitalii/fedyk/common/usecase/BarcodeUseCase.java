package org.vitalii.fedyk.common.usecase;

import org.vitalii.fedyk.FileInfo;

/** Use case for generating barcode images from ISBN codes. */
public interface BarcodeUseCase {
  /**
   * Generates a barcode image for the specified ISBN.
   *
   * @param isbn the ISBN string for which the barcode will be generated
   * @return a {@link FileInfo} containing the barcode image data and file extension
   */
  FileInfo generateImageBarcode(String isbn);
}
