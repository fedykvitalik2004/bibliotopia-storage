package org.vitalii.fedyk.minio.exception;

import org.vitalii.fedyk.common.exception.LocalizedException;

/** An exception class to represent errors that occur during file storage operations. */
public class FileStorageException extends LocalizedException {
  /**
   * Constructs a new {@code FileStorageException} with the specified detail message, message
   * arguments for localization, and cause.
   *
   * @param message the detail message or message key for localization
   * @param messageArguments the arguments to be used with the message for localization
   * @param cause the cause of this exception
   */
  public FileStorageException(
      final String message, final Object[] messageArguments, final Throwable cause) {
    super(message, messageArguments, cause);
  }

  /**
   * Constructs a new {@code FileStorageException} with the specified detail message and message
   * arguments for localization.
   *
   * @param message the detail message or message key for localization
   * @param messageArguments the arguments to be used with the message for localization
   */
  public FileStorageException(final String message, final Object[] messageArguments) {
    super(message, messageArguments);
  }
}
