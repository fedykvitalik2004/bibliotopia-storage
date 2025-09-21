package org.vitalii.fedyk.common.exception;

/**
 * Exception thrown when a barcode cannot be generated.
 */
public class BarcodeGenerationException extends RuntimeException {
  public BarcodeGenerationException(String message, Throwable cause) {
    super(message, cause);
  }
}
