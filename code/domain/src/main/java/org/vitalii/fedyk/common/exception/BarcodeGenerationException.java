package org.vitalii.fedyk.common.exception;

/**
 * Exception thrown when a barcode cannot be generated.
 */
public class BarcodeGenerationException extends LocalizedException {
  /**
   * Constructs a new {@code BarcodeGenerationException} with the specified
   * detail message, message arguments for localization, and cause.
   *
   * @param message the detail message or message key for localization
   * @param messageArguments the arguments to be used with
   *                         the message for localization
   * @param cause the cause of this exception
   */
  public BarcodeGenerationException(final String message,
                                    final Object[] messageArguments,
                                    final Throwable cause) {
    super(message, messageArguments, cause);
  }
}
