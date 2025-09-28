package org.vitalii.fedyk.common.exception;

/**
 * Runtime exception that indicates a method has been passed an illegal or inappropriate argument,
 * with support for localized messages.
 */
public class LocalizedIllegalArgumentException extends LocalizedException {
  /**
   * Constructs a new {@code LocalizedIllegalArgumentException} with the specified detail message,
   * message arguments for localization, and cause.
   *
   * @param message the detail message or message key for localization
   * @param messageArguments the arguments to be used with the message for localization
   * @param cause the cause of this exception
   */
  public LocalizedIllegalArgumentException(
      final String message, final Object[] messageArguments, final Throwable cause) {
    super(message, messageArguments, cause);
  }

  /**
   * Constructs a new {@code LocalizedIllegalArgumentException} with the specified detail message
   * and message arguments for localization.
   *
   * @param message the detail message or message key for localization
   * @param messageArguments the arguments to be used with the message for localization
   */
  public LocalizedIllegalArgumentException(final String message, final Object[] messageArguments) {
    super(message, messageArguments);
  }
}
