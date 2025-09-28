package org.vitalii.fedyk.minio.usecase;

import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.common.usecase.FileProcessingUseCase;
import org.vitalii.fedyk.minio.exception.FileProcessingException;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import org.vitalii.fedyk.minio.model.StorageLocation;
import org.vitalii.fedyk.minio.repository.MinIoObjectInfoRepository;

@Service
public class MinIoObjectInfoUseCaseImpl implements MinIoObjectInfoUseCase {
  private final FileStorageUseCase fileStorageUseCase;
  private final MinIoObjectInfoRepository repository;
  private final FileProcessingUseCase fileProcessingUseCase;

  @Autowired
  public MinIoObjectInfoUseCaseImpl(
      FileStorageUseCase fileStorageUseCase,
      MinIoObjectInfoRepository repository,
      FileProcessingUseCase fileProcessingUseCase) {
    this.fileStorageUseCase = fileStorageUseCase;
    this.repository = repository;
    this.fileProcessingUseCase = fileProcessingUseCase;
  }

  @Override
  public MinIoObjectInfo save(final String appName, final MultipartFile file) {
    final String fileName = UUID.randomUUID() + FileUtils.getExtension(file.getName());
    final StorageLocation storageLocation = new StorageLocation(appName, fileName);
    byte[] fileBytes = getFileBytes(file);
    final String contentType = file.getContentType();

    if (contentType != null && contentType.startsWith("image/")) {
      fileBytes = fileProcessingUseCase.compressImage(fileBytes);
    }

    final FileUpload fileUpload =
        new FileUpload(fileName, fileBytes, contentType, fileBytes.length);
    final FileStorageResult fileStorageResult =
        fileStorageUseCase.uploadFile(storageLocation, fileUpload);

    final MinIoObjectInfo toSave =
        MinIoObjectInfo.builder()
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
    final MinIoObjectInfo minIoObjectInfo = repository.findById(id).orElseThrow();
    final StorageLocation storageLocation =
        new StorageLocation(minIoObjectInfo.getBucketName(), minIoObjectInfo.getObjectName());
    final String url = fileStorageUseCase.getFileAccessUrl(storageLocation);
    return minIoObjectInfo.toBuilder().url(url).build();
  }

  public byte[] getFileBytes(final MultipartFile file) {
    byte[] fileBytes = null;
    try {
      fileBytes = file.getBytes();
    } catch (IOException e) {
      throw new FileProcessingException("exception.file_processing.failed_reading_bytes", null);
    }
    return fileBytes;
  }

  @UtilityClass
  private static class FileUtils {
    private static final Pattern EXTENSION_PATTERN = Pattern.compile("\\.([^.]+)$");

    public static String getExtension(final String fileName) {
      if (fileName == null || fileName.isEmpty()) {
        return "";
      }

      final Matcher matcher = EXTENSION_PATTERN.matcher(fileName);
      if (matcher.find()) {
        return "." + matcher.group(1);
      } else {
        return "";
      }
    }
  }
}
