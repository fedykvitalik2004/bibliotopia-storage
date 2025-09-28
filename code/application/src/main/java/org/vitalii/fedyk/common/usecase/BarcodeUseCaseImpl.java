package org.vitalii.fedyk.common.usecase;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import java.io.ByteArrayOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.FileInfo;
import org.vitalii.fedyk.common.exception.BarcodeGenerationException;

/** {@inheritDoc} */
@Service
@Slf4j
public class BarcodeUseCaseImpl implements BarcodeUseCase {
  private final String extension = "png";

  @Override
  public FileInfo generateImageBarcode(final String isbn) {
    try {
      final BitMatrix bitMatrix =
          new MultiFormatWriter().encode(isbn, BarcodeFormat.EAN_13, 300, 100);

      try (final ByteArrayOutputStream os = new ByteArrayOutputStream()) {
        MatrixToImageWriter.writeToStream(bitMatrix, extension, os);
        return new FileInfo(extension, os.toByteArray());
      }
    } catch (WriterException exception) {
      log.error("Invalid ISBN format: {}", isbn, exception);
      throw new BarcodeGenerationException(
          "exception.barcode.invalid_format", new Object[] {isbn}, exception);
    } catch (Exception e) {
      log.error("Unexpected error generating barcode for ISBN: {}", isbn, e);
      throw new BarcodeGenerationException("exception.barcode.general", null, e);
    }
  }
}
