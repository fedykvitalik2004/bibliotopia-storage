package org.vitalii.fedyk.generation.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.FileInfo;
import org.vitalii.fedyk.common.usecase.BarcodeUseCase;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.StorageLocation;
import org.vitalii.fedyk.minio.repository.FileStorageRepository;

/**
 {@inheritDoc}
 */
@Service
public class GenerationProcessingUseCaseImpl implements GenerationProcessingUseCase {
  private final BarcodeUseCase barcodeUseCase;

  private final FileStorageRepository fileStorageRepository;

  @Value("${minio.barcode-bucket}")
  private String barcodeBucket;

  @Autowired
  public GenerationProcessingUseCaseImpl(final BarcodeUseCase barcodeUseCase,
                                         final FileStorageRepository fileStorageRepository) {
    this.barcodeUseCase = barcodeUseCase;
    this.fileStorageRepository = fileStorageRepository;
  }

  @Override
  public String generateBarcodeAndReturnUrl(final String isbn) {
    final FileInfo generatedFileInfo = barcodeUseCase.generateImageBarcode(isbn);

    final StorageLocation storageLocation = new StorageLocation(barcodeBucket, isbn);
    final FileUpload fileUpload = new FileUpload(isbn + "." +  generatedFileInfo.extension(),
            generatedFileInfo.content(),
            "image/" + generatedFileInfo.extension(),
            generatedFileInfo.content().length
    );
    final FileStorageResult saved = fileStorageRepository.store(storageLocation, fileUpload);

    return saved.accessUrl();
  }
}
