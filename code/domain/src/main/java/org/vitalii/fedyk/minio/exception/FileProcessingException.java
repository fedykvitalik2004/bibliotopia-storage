package org.vitalii.fedyk.minio.exception;

/**
 * Thrown to indicate an error occurred during file processing operations.
 */
public class FileProcessingException extends RuntimeException {
  public FileProcessingException(String message, Throwable cause) {
    super(message, cause);
  }
}
