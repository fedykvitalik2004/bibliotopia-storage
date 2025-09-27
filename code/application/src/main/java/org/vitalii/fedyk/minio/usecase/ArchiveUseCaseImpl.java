package org.vitalii.fedyk.minio.usecase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.vitalii.fedyk.common.exception.LocalizedIllegalArgumentException;
import org.vitalii.fedyk.minio.exception.FileProcessingException;
import org.vitalii.fedyk.minio.repository.FileStorageRepository;

/**
 * {@inheritDoc}
 */
@Service
@Slf4j
public class ArchiveUseCaseImpl implements ArchiveUseCase {
  private final FileStorageRepository fileStorageRepository;

  @Autowired
  public ArchiveUseCaseImpl(final FileStorageRepository fileStorageRepository) {
    this.fileStorageRepository = fileStorageRepository;
  }

  @Override
  public void streamBucketAsStream(String bucketName, OutputStream outputStream) {
    validateInputs(bucketName, outputStream);

    try (final ZipOutputStream zipOutputStream = createZipOutputStream(outputStream)) {
      final List<String> fileNames = fileStorageRepository.getFileNames(bucketName);
      final Iterator<String> fileNameIterator = fileNames.iterator();
      while (fileNameIterator.hasNext()) {
        final String fileName = fileNameIterator.next();
        processFile(bucketName, fileName, zipOutputStream);
      }
    } catch (IOException e) {
      throw new FileProcessingException("exception.file_processing.failed_creating", null);
    }
  }

  private void validateInputs(String bucketName, OutputStream outputStream) {
    if (bucketName == null) {
      throw new LocalizedIllegalArgumentException("exception.file_processing.failed_creating", null);
    }
    if (outputStream == null) {
      throw new LocalizedIllegalArgumentException("exception.not_null_output_stream", null);
    }
  }

  private void processFile(String bucketName, String fileName, ZipOutputStream zipOutputStream) throws IOException {
    log.debug("Processing file '{}' from bucket '{}'", fileName, bucketName);

    final ZipEntry zipEntry = createZipEntry(fileName);

    try (InputStream inputStream = fileStorageRepository.getObjectStream(bucketName, fileName)) {
      zipOutputStream.putNextEntry(zipEntry);
      inputStream.transferTo(zipOutputStream);
      zipOutputStream.closeEntry();

      log.debug("Successfully processed file '{}'", fileName);
    }
  }

  private ZipEntry createZipEntry(String fileName) {
    final ZipEntry zipEntry = new ZipEntry(fileName);
    zipEntry.setTime(System.currentTimeMillis());
    return zipEntry;
  }

  private ZipOutputStream createZipOutputStream(OutputStream outputStream) {
    final ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);
    zipOutputStream.setMethod(ZipOutputStream.DEFLATED);
    zipOutputStream.setLevel(Deflater.BEST_COMPRESSION);
    return zipOutputStream;
  }
}
