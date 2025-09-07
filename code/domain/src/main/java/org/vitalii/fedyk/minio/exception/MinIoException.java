package org.vitalii.fedyk.minio.exception;

/**
 * Custom runtime exception for MinIO-related errors.
 */
public class MinIoException extends RuntimeException {
  public MinIoException(String message) {
    super(message);
  }

  public MinIoException(String message, Throwable cause) {
    super(message, cause);
  }
}