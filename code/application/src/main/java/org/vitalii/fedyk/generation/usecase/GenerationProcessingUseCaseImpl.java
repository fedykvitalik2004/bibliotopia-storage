package org.vitalii.fedyk.generation.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.common.usecase.BarcodeUseCase;
import org.vitalii.fedyk.generation.model.FileInfo;
import org.vitalii.fedyk.minio.model.MinIoSaveRequest;
import org.vitalii.fedyk.minio.model.StorageInfo;
import org.vitalii.fedyk.minio.usecase.MinIoObjectInfoUseCase;

/** {@inheritDoc} */
@Service
public class GenerationProcessingUseCaseImpl implements GenerationProcessingUseCase {
  private final BarcodeUseCase barcodeUseCase;

  private final MinIoObjectInfoUseCase minIoObjectInfoUseCase;

  @Value("${minio.barcode-bucket}")
  private String barcodeBucket;

  @Autowired
  public GenerationProcessingUseCaseImpl(
      final BarcodeUseCase barcodeUseCase, final MinIoObjectInfoUseCase minIoObjectInfoUseCase) {
    this.barcodeUseCase = barcodeUseCase;
    this.minIoObjectInfoUseCase = minIoObjectInfoUseCase;
  }

  @Override
  public String generateBarcodeAndReturnUrl(final String isbn) {
    final FileInfo generatedFileInfo = this.barcodeUseCase.generateImageBarcode(isbn);

    final MinIoSaveRequest minIoSaveRequest =
        new MinIoSaveRequest(
            isbn + "." + generatedFileInfo.extension(),
            "image/" + generatedFileInfo.extension(),
            generatedFileInfo.length(),
            generatedFileInfo.content());
    final StorageInfo saved = this.minIoObjectInfoUseCase.save(barcodeBucket, minIoSaveRequest);

    return saved.getUrl();
  }
}
