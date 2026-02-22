package org.vitalii.fedyk.minio.usecase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.minio.exception.FileProcessingException;
import org.vitalii.fedyk.minio.model.StorageInfo;
import org.vitalii.fedyk.minio.repository.FileStorageRepository;
import org.vitalii.fedyk.minio.repository.StorageInfoRepository;

/** {@inheritDoc} */
@Service
@Slf4j
public class ArchiveUseCaseImpl implements ArchiveUseCase {
  private final FileStorageRepository fileStorageRepository;

  private final StorageInfoRepository storageInfoRepository;

  @Autowired
  public ArchiveUseCaseImpl(
      FileStorageRepository fileStorageRepository, StorageInfoRepository storageInfoRepository) {
    this.fileStorageRepository = fileStorageRepository;
    this.storageInfoRepository = storageInfoRepository;
  }

  @Override
  public void streamFilesFromBucket(
      final String bucketName, final List<UUID> storageInfoIds, final OutputStream outputStream) {
    final List<String> fileNames =
        storageInfoIds.stream()
            .map(id -> this.storageInfoRepository.findById(id).orElse(null))
            .filter(Objects::nonNull)
            .filter(StorageInfo::isComplete)
            .map(StorageInfo::getObjectName)
            .toList();

    try (final BufferedOutputStream bufferedOutputStream =
            new BufferedOutputStream(outputStream, 64 * 1024);
        final ZipOutputStream zipOutputStream = createZipOutputStream(bufferedOutputStream)) {
      for (final String fileName : fileNames) {
        processFile(bucketName, fileName, zipOutputStream);
      }
    } catch (IOException e) {
      throw new FileProcessingException("exception.file_processing.failed_creating", null);
    }
  }

  private void processFile(String bucketName, String fileName, ZipOutputStream zipOutputStream)
      throws IOException {
    log.debug("Processing file '{}' from bucket '{}'", fileName, bucketName);

    final ZipEntry zipEntry = this.createZipEntry(fileName);

    try (final InputStream inputStream =
            this.fileStorageRepository.getObjectStream(bucketName, fileName); // todo: Heavy.
        final BufferedInputStream bufferedInputStream =
            new BufferedInputStream(inputStream, 64 * 1024)) {
      zipOutputStream.putNextEntry(zipEntry);

      byte[] buffer = new byte[16 * 1024];
      int bytesRead;

      while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
        zipOutputStream.write(buffer, 0, bytesRead);
      }

      zipOutputStream.closeEntry();

      log.debug("Successfully processed file '{}'", fileName);
    }
  }

  private ZipEntry createZipEntry(String fileName) {
    final ZipEntry zipEntry = new ZipEntry(fileName);
    zipEntry.setTime(System.currentTimeMillis()); // todo: improve time write
    return zipEntry;
  }

  private ZipOutputStream createZipOutputStream(OutputStream outputStream) {
    final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
    zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
    zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
    return zipOutputStream;
  }
}
