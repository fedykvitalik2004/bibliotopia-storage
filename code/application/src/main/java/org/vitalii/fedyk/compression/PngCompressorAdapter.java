package org.vitalii.fedyk.compression;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import org.springframework.stereotype.Component;

/** Adapter implementation of {@link ImageCompressorPort} for PNG images. */
@Component
public class PngCompressorAdapter implements ImageCompressorPort {

  @Override
  public byte[] compress(final InputStream inputStream) throws IOException {
    try (final ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
      final BufferedImage image = ImageIO.read(inputStream);
      final ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();

      try (final ImageOutputStream ios = ImageIO.createImageOutputStream(baos)) {
        writer.setOutput(ios);

        final ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(0.6f);

        writer.write(null, new javax.imageio.IIOImage(image, null, null), param);
        writer.dispose();
      }
      return baos.toByteArray();
    }
  }

  @Override
  public Set<String> getSupportedNames() {
    return Set.of("png");
  }
}
