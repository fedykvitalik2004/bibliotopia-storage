package org.vitalii.fedyk.minio.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.StorageApi;
import org.openapitools.model.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.mapper.MinIoObjectInfoMapper;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import org.vitalii.fedyk.minio.usecase.ArchiveUseCase;
import org.vitalii.fedyk.minio.usecase.MinIoObjectInfoUseCase;

/**
 * REST controller that handles storage-related operations.
 * Implements {@link StorageApi} to provide endpoints for file upload.
 */
@RestController
@Slf4j
public class StorageController implements StorageApi {
  private MinIoObjectInfoUseCase minIoObjectInfoUseCase;
  private ArchiveUseCase archiveUseCase;
  private MinIoObjectInfoMapper mapper;

  @Override
  public ResponseEntity<FileUploadResponse> uploadFile(final String xAppName, final MultipartFile file) {
    final MinIoObjectInfo saved = minIoObjectInfoUseCase.save(xAppName, file);
    return ResponseEntity.ok(mapper.toFileUploadResponse(saved));
  }

  @Override
  public ResponseEntity<FileUploadResponse> findById(final String xAppName, final UUID id) {
    final MinIoObjectInfo retrieved = minIoObjectInfoUseCase.findById(id);
    return ResponseEntity.ok(mapper.toFileUploadResponse(retrieved));
  }

  @SneakyThrows
  @Override
  public ResponseEntity<Resource> downloadZip(final String xAppName) {
    //todo: replace it
    final File tempFile = File.createTempFile(xAppName + "-", ".zip");

    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
      archiveUseCase.streamBucketAsStream(xAppName, fos);
    }

    final Resource resource = new FileSystemResource(tempFile);

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + xAppName + ".zip")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .contentLength(resource.contentLength())
            .body(resource);
  }

  @Autowired
  public void setMinIoUseCase(final MinIoObjectInfoUseCase minIoObjectInfoUseCase) {
    this.minIoObjectInfoUseCase = minIoObjectInfoUseCase;
  }

  @Autowired
  public void setArchiveUseCase(ArchiveUseCase archiveUseCase) {
    this.archiveUseCase = archiveUseCase;
  }

  @Autowired
  public void setMapper(final MinIoObjectInfoMapper mapper) {
    this.mapper = mapper;
  }
}
