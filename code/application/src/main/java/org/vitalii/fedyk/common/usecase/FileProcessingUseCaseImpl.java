package org.vitalii.fedyk.common.usecase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

/** {@inheritDoc} */
@Service
@Slf4j
public class FileProcessingUseCaseImpl implements FileProcessingUseCase {

  @Override
  public byte[] compressImage(@NonNull final byte[] imageBytes) {
    try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
      Thumbnails.of(new ByteArrayInputStream(imageBytes))
          .scale(1.0)
          .outputQuality(0.8)
          .toOutputStream(os);
      return os.toByteArray();
    } catch (IOException e) {
      log.error("Image was not processed. Error occurred", e);
      return imageBytes;
    }
  }
}
