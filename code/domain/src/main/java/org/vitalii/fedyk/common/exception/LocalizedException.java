package org.vitalii.fedyk.common.exception;

import lombok.Getter;

/**
 * A base runtime exception that supports localization of error messages.
 */
@Getter
public class LocalizedException extends RuntimeException {
  /**
   * The arguments to be used with the exception message for localization
   * or formatting purposes.
   */
  private final transient Object[] messageArguments;

  /**
   * Constructs a new {@code LocalizedException} with the specified
   * detail message, message arguments for localization, and cause.
   *
   * @param message the detail message or message key for localization
   * @param args the arguments to be used with
   *                         the message for localization
   * @param cause the cause of this exception
   */
  public LocalizedException(final String message,
                            final Object[] args,
                            final Throwable cause) {
    super(message, cause);
    this.messageArguments = args;
  }

  /**
   * Constructs a new {@code LocalizedException} with the specified
   * detail message and message arguments for localization.
   *
   * @param message the detail message or message key for localization
   * @param args the arguments to be used with
   *                         the message for localization
   */
  public LocalizedException(final String message,
                            final Object[] args) {
    super(message);
    this.messageArguments = args;
  }

}
