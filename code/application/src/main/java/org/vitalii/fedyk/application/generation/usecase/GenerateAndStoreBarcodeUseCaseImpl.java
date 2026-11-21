package org.vitalii.fedyk.application.generation.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.domain.common.usecase.GenerateBarcodeUseCase;
import org.vitalii.fedyk.domain.generation.model.FileInfo;
import org.vitalii.fedyk.domain.generation.usecase.GenerateAndStoreBarcodeUseCase;
import org.vitalii.fedyk.domain.minio.model.FileUpload;
import org.vitalii.fedyk.domain.minio.model.StorageInfo;
import org.vitalii.fedyk.domain.minio.usecase.FileManagementService;

/** {@inheritDoc} */
@Service
public class GenerateAndStoreBarcodeUseCaseImpl implements GenerateAndStoreBarcodeUseCase {
  private final GenerateBarcodeUseCase generateBarcodeUseCase;

  private final FileManagementService fileManagementService;

  @Value("${minio.barcode-bucket}")
  private String barcodeBucket;

  @Autowired
  public GenerateAndStoreBarcodeUseCaseImpl(
      final GenerateBarcodeUseCase generateBarcodeUseCase,
      final FileManagementService fileManagementService) {
    this.generateBarcodeUseCase = generateBarcodeUseCase;
    this.fileManagementService = fileManagementService;
  }

  @Override
  public String generateBarcodeAndReturnUrl(final String isbn) {
    final FileInfo generatedFileInfo = this.generateBarcodeUseCase.generateImageBarcode(isbn);

    final FileUpload fileUpload =
        new FileUpload(
            isbn + "." + generatedFileInfo.extension(),
            generatedFileInfo.content(),
            "image/" + generatedFileInfo.extension(),
            generatedFileInfo.length());
    final StorageInfo saved = this.fileManagementService.save(barcodeBucket, fileUpload);

    return saved.getUrl();
  }
}
