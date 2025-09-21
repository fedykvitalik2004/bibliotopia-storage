package org.vitalii.fedyk.common.usecase;

import org.springframework.lang.NonNull;

/**
 * A use case interface for processing files, such as image compression.
 */
public interface FileProcessingUseCase {
  byte[] compressImage(@NonNull byte[] imageBytes);
}
