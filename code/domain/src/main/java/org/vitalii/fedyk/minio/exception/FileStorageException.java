package org.vitalii.fedyk.minio.exception;

/**
 * An exception class to represent errors that occur during file storage operations.
 */
public class FileStorageException extends RuntimeException {
  public FileStorageException(String message) {
    super(message);
  }

  public FileStorageException(String message, Throwable cause) {
    super(message, cause);
  }
}