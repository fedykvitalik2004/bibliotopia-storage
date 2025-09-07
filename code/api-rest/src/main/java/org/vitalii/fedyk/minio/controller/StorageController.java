package org.vitalii.fedyk.minio.controller;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.StorageApi;
import org.openapitools.model.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.vitalii.fedyk.minio.mapper.MinIoObjectInfoMapper;
import org.vitalii.fedyk.minio.model.MinIoObjectInfo;
import org.vitalii.fedyk.minio.usecase.MinIoInteractionUseCase;
import org.vitalii.fedyk.minio.usecase.MinIoObjectInfoUseCase;

/**
 * REST controller that handles storage-related operations.
 * <p>
 * Implements {@link StorageApi} to provide endpoints for file upload
 * and delegates the actual storage logic to {@link MinIoInteractionUseCase}.
 * </p>
 */
@RestController
@Slf4j
public class StorageController implements StorageApi {
  private MinIoObjectInfoUseCase minIoObjectInfoUseCase;
  private MinIoObjectInfoMapper mapper;

  @Override
  public ResponseEntity<FileUploadResponse> uploadFile(final String xAppName, final MultipartFile file) {
    final MinIoObjectInfo response = minIoObjectInfoUseCase.save(xAppName, file);
    return ResponseEntity.ok(mapper.toFileUploadResponse(response));
  }

  @Autowired
  public void setMinIoUseCase(final MinIoObjectInfoUseCase minIoObjectInfoUseCase) {
    this.minIoObjectInfoUseCase = minIoObjectInfoUseCase;
  }

  @Autowired
  public void setMapper(final MinIoObjectInfoMapper mapper) {
    this.mapper = mapper;
  }
}
