package org.vitalii.fedyk.application.common.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayInputStream;
import javax.imageio.ImageIO;
import org.junit.jupiter.api.Test;
import org.vitalii.fedyk.domain.common.exception.BarcodeGenerationException;
import org.vitalii.fedyk.domain.generation.model.FileInfo;

class GenerateBarcodeUseCaseImplTest {
  private final GenerateBarcodeUseCaseImpl generateBarcodeUseCase =
      new GenerateBarcodeUseCaseImpl();

  @Test
  void generateImageBarcode_shouldReturnPngImageForValidIsbn() throws Exception {
    // Given
    final var isbn = "5901234123457";

    // When
    final FileInfo fileInfo = generateBarcodeUseCase.generateImageBarcode(isbn);
    final byte[] imageBytes = fileInfo.content().readAllBytes();

    // Then
    assertEquals("png", fileInfo.extension());
    assertEquals(imageBytes.length, fileInfo.length());
    assertNotNull(ImageIO.read(new ByteArrayInputStream(imageBytes)));
  }

  @Test
  void generateImageBarcode_shouldThrowBarcodeGenerationExceptionForInvalidIsbn() {
    // Given
    final var isbn = "";

    // When
    final BarcodeGenerationException exception =
        assertThrows(
            BarcodeGenerationException.class,
            () -> generateBarcodeUseCase.generateImageBarcode(isbn));

    // Then
    assertEquals("exception.barcode.general", exception.getMessage());
    assertNull(exception.getMessageArguments());
    assertNotNull(exception.getCause());
  }
}
