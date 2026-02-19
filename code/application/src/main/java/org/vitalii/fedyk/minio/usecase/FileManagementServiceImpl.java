package org.vitalii.fedyk.minio.usecase;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.compression.CompressorFactory;
import org.vitalii.fedyk.compression.ImageCompressorPort;
import org.vitalii.fedyk.minio.model.ChunkUploadRequest;
import org.vitalii.fedyk.minio.model.CompleteRequest;
import org.vitalii.fedyk.minio.model.FileStorageResult;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.InitiateUploadResponse;
import org.vitalii.fedyk.minio.model.StorageInfo;
import org.vitalii.fedyk.minio.model.StorageLocation;
import org.vitalii.fedyk.minio.repository.StorageInfoRepository;

@Slf4j
@Service
public class FileManagementServiceImpl implements FileManagementService {
  private final FileStorageService fileStorageService;

  private final StorageInfoRepository storageInfoRepository;

  private final CompressorFactory compressorFactory;

  @Autowired
  public FileManagementServiceImpl(
      FileStorageService fileStorageService,
      StorageInfoRepository storageInfoRepository,
      CompressorFactory compressorFactory) {
    this.fileStorageService = fileStorageService;
    this.storageInfoRepository = storageInfoRepository;
    this.compressorFactory = compressorFactory;
  }

  @SneakyThrows
  @Override
  public StorageInfo save(final String appName, final FileUpload fileUpload) {
    final String newFileName = this.getNewFileName(fileUpload.fileName());

    // Compress based on its type, if it is possible.
    final String fileFormat = FileUtils.getExtension(newFileName);
    long contentLength = fileUpload.size();
    InputStream inputStream = fileUpload.content();

    if (compressorFactory.isAvailableFor(fileFormat)) {
      final ImageCompressorPort imageCompressorPort =
          this.compressorFactory.getCompressorFor(fileFormat);
      final byte[] fileBytes = imageCompressorPort.compress(fileUpload.content());
      contentLength = fileBytes.length;
      inputStream = new ByteArrayInputStream(fileBytes);
    }

    // Save into S3.
    final FileUpload updatedFileUpdate =
        new FileUpload(newFileName, inputStream, fileUpload.contentType(), contentLength);
    final FileStorageResult fileStorageResult =
        this.fileStorageService.uploadFile(appName, updatedFileUpdate);

    // Save info in DB.
    final StorageInfo toSave =
        StorageInfo.builder()
            .bucketName(fileStorageResult.location().bucketName())
            .objectName(newFileName)
            .complete(true)
            .build();
    final StorageInfo saved = this.storageInfoRepository.save(toSave);
    saved.setUrl(fileStorageResult.accessUrl());
    return saved;
  }

  @Override
  public StorageInfo findById(final UUID id) {
    final StorageInfo storageInfo = this.storageInfoRepository.findById(id).orElseThrow();
    final StorageLocation storageLocation =
        new StorageLocation(storageInfo.getBucketName(), storageInfo.getObjectName());
    final String url = this.fileStorageService.getFileAccessUrl(storageLocation);
    return storageInfo.toBuilder().url(url).build();
  }

  @Override
  public InitiateUploadResponse initiateUpload(final String fileName, final String appName) {
    final String newFileName = this.getNewFileName(fileName);
    final String uploadId =
        this.fileStorageService.createMultiPartUpload(
            StorageLocation.builder().bucketName(appName).objectKey(newFileName).build());

    final StorageInfo saved =
        this.storageInfoRepository.save(
            StorageInfo.builder()
                .bucketName(appName)
                .objectName(newFileName)
                .complete(false)
                .build());
    return InitiateUploadResponse.builder()
        .storageInfoId(saved.getId())
        .fileName(saved.getObjectName())
        .uploadId(uploadId)
        .build();
  }

  @Override
  public String uploadChunk(final String appName, final ChunkUploadRequest request) {
    return this.fileStorageService.uploadChunk(
        appName,
        request.inputStream(),
        request.chunkSize(),
        request.uploadId(),
        request.chunkNumber(),
        request.fileName());
  }

  @Override
  public StorageInfo completeUpload(final String appName, final CompleteRequest completeRequest) {
    final String accessUrl = this.fileStorageService.completeUpload(appName, completeRequest);
    final Optional<StorageInfo> storageInfoMaybe =
        this.storageInfoRepository
            .findByBucketNameAndObjectName(appName, completeRequest.fileName())
            .map(
                storageInfo -> {
                  storageInfo.setComplete(true);
                  return storageInfo;
                });
    final StorageInfo storageInfo = storageInfoMaybe.orElseThrow();
    final StorageInfo saved = this.storageInfoRepository.save(storageInfo);
    return saved.toBuilder().url(accessUrl).build();
  }

  private String getNewFileName(final String fileName) {
    return UUID.randomUUID() + "." + FileUtils.getExtension(fileName);
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
        return matcher.group(1);
      } else {
        return "";
      }
    }
  }
}
