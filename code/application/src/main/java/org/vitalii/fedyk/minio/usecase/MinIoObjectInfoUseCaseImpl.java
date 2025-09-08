package org.vitalii.fedyk.minio.usecase;

import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import org.vitalii.fedyk.minio.model.StorageLocation;
import org.vitalii.fedyk.minio.repository.MinIoObjectInfoRepository;

@Service
public class MinIoObjectInfoUseCaseImpl implements MinIoObjectInfoUseCase {
  private final FileStorageUseCase fileStorageUseCase;
  private final MinIoObjectInfoRepository repository;

  @Autowired
  public MinIoObjectInfoUseCaseImpl(FileStorageUseCase fileStorageUseCase, MinIoObjectInfoRepository repository) {
    this.fileStorageUseCase = fileStorageUseCase;
    this.repository = repository;
  }

  @Override
  public MinIoObjectInfo save(final String appName, final MultipartFile file) {
    final String fileName = UUID.randomUUID().toString();
    final StorageLocation storageLocation = new StorageLocation(appName, fileName);
    byte[] fileBytes = null;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    final FileUpload fileUpload = new FileUpload(fileName, fileBytes, file.getContentType(), file.getSize());
    final FileStorageResult fileStorageResult = fileStorageUseCase.uploadFile(storageLocation, fileUpload);

    final MinIoObjectInfo toSave = MinIoObjectInfo.builder()
            .bucketName(fileStorageResult.location().bucketName())
            .objectName(fileStorageResult.location().objectKey())
            .size(fileStorageResult.size())
            .contentType(fileStorageResult.contentType())
            .build();
    final MinIoObjectInfo saved = repository.save(toSave);
    saved.setUrl(fileStorageResult.accessUrl());
    return saved;
  }

  @Override
  public MinIoObjectInfo findById(final UUID id) {
    final MinIoObjectInfo minIoObjectInfo = repository.findById(id)
            .orElseThrow();
    final StorageLocation storageLocation = new StorageLocation(minIoObjectInfo.getBucketName(), minIoObjectInfo.getObjectName());
    final String url = fileStorageUseCase.getFileAccessUrl(storageLocation);
    return minIoObjectInfo.toBuilder()
            .url(url)
            .build();
  }
}
