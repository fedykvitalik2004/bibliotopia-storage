package org.vitalii.fedyk.compression;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;

/** Adapter implementation of {@link ImageCompressorPort} for JPEG images. */
@Component
public class JpegCompressorAdapter implements ImageCompressorPort {
  @Override
  public byte[] compress(InputStream inputStream) throws IOException {
    try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      Thumbnails.of(inputStream).scale(1.0).outputQuality(0.8).toOutputStream(os);
      return os.toByteArray();
    }
  }

  @Override
  public Set<String> getSupportedNames() {
    return Set.of("jpeg", "jpg");
  }
}
