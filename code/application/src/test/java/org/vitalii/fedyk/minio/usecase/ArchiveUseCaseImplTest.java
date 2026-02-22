package org.vitalii.fedyk.minio.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.vitalii.fedyk.minio.model.StorageInfo;
import org.vitalii.fedyk.minio.repository.FileStorageRepository;
import org.vitalii.fedyk.minio.repository.StorageInfoRepository;

@ExtendWith(MockitoExtension.class)
class ArchiveUseCaseImplTest {
  @Mock private FileStorageRepository fileStorageRepository;

  @Mock private StorageInfoRepository storageInfoRepository;

  @InjectMocks private ArchiveUseCaseImpl archiveUseCase;

  @Test
  void shouldZipOnlyCompleteFiles() throws IOException {
    // Given
    final var bucketName = "bucket";
    final var fileName = "text.txt";
    final var fileContent = "Hello World";
    final var uuids = List.of(UUID.randomUUID());
    final var byteArrayOutputStream = new ByteArrayOutputStream();

    final var storageInfo = StorageInfo.builder().objectName(fileName).complete(true).build();

    final var inputStream = new ByteArrayInputStream(fileContent.getBytes(StandardCharsets.UTF_8));

    when(storageInfoRepository.findById(uuids.get(0))).thenReturn(Optional.of(storageInfo));
    when(fileStorageRepository.getObjectStream(bucketName, fileName)).thenReturn(inputStream);

    // When
    archiveUseCase.streamFilesFromBucket(bucketName, uuids, byteArrayOutputStream);

    // Then
    final var actualBytes = byteArrayOutputStream.toByteArray();

    try (ByteArrayInputStream bais = new ByteArrayInputStream(actualBytes);
        ZipInputStream zis = new ZipInputStream(bais)) {

      ZipEntry entry = zis.getNextEntry();
      assertNotNull(entry);
      assertEquals(fileName, entry.getName());

      byte[] contentBytes = zis.readAllBytes();
      String actualContent = new String(contentBytes, StandardCharsets.UTF_8);
      assertEquals(fileContent, actualContent);
    } catch (IOException e) {
      fail();
    }

    verify(storageInfoRepository).findById(uuids.get(0));
    verify(fileStorageRepository).getObjectStream(bucketName, fileName);
  }
}
