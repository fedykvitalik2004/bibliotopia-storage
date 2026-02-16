package org.vitalii.fedyk.compression;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

class PngCompressorAdapterTest {
  private final ImageCompressorPort pngCompressor = new PngCompressorAdapter();

  @Test
  @SneakyThrows
  void compress_shouldProduceReadableAndModifiedPngFromInputStream() {
    // Given
    final var originalInputStream = getClass().getResourceAsStream("/images/image.png");
    assertNotNull(originalInputStream);

    byte[] originalBytes;
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      originalInputStream.transferTo(os);
      originalBytes = os.toByteArray();
    }

    // When
    try (var inputForCompression = new ByteArrayInputStream(originalBytes)) {
      final byte[] compressed = this.pngCompressor.compress(inputForCompression);

      // Then
      assertNotNull(compressed);
      assertTrue(compressed.length > 0);

      try (var bis = new ByteArrayInputStream(compressed)) {
        final var bufferedImage = ImageIO.read(bis);
        assertNotNull(bufferedImage);
        assertTrue(bufferedImage.getWidth() > 0 && bufferedImage.getHeight() > 0);
      }

      assertFalse(Arrays.equals(originalBytes, compressed));
      assertTrue(compressed.length <= originalBytes.length);
    }
  }
}
