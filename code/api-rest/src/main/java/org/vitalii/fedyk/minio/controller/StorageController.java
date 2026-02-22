package org.vitalii.fedyk.minio.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.api.StorageApi;
import org.openapitools.model.ChunkUploadRequestDto;
import org.openapitools.model.CompleteRequestDto;
import org.openapitools.model.DownloadZipRequestDto;
import org.openapitools.model.InitiateUploadResponseDto;
import org.openapitools.model.StorageInfoDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.vitalii.fedyk.minio.mapper.ChunkUploadRequestMapper;
import org.vitalii.fedyk.minio.mapper.CompleteRequestMapper;
import org.vitalii.fedyk.minio.mapper.InitiateUploadResponseMapper;
import org.vitalii.fedyk.minio.mapper.StorageInfoDtoMapper;
import org.vitalii.fedyk.minio.model.ChunkUploadRequest;
import org.vitalii.fedyk.minio.model.CompleteRequest;
import org.vitalii.fedyk.minio.model.FileUpload;
import org.vitalii.fedyk.minio.model.InitiateUploadResponse;
import org.vitalii.fedyk.minio.model.StorageInfo;
import org.vitalii.fedyk.minio.usecase.ArchiveUseCase;
import org.vitalii.fedyk.minio.usecase.FileManagementService;

/**
 * REST controller that handles storage-related operations. Implements {@link StorageApi} to provide
 * endpoints for file upload.
 */
@RestController
@Slf4j
@AllArgsConstructor
public class StorageController implements StorageApi {
  private final FileManagementService fileManagementService;

  private final ArchiveUseCase archiveUseCase;

  private final StorageInfoDtoMapper storageInfoDtoMapper;

  private final CompleteRequestMapper completeRequestMapper;

  private final InitiateUploadResponseMapper initiateUploadResponseMapper;

  private final ChunkUploadRequestMapper chunkUploadRequestMapper;

  @SneakyThrows
  @Override
  @RequiredHeader("X-App-Name")
  public ResponseEntity<StorageInfoDto> uploadFile(
      final MultipartFile file, final String xAppName) {
    final FileUpload fileUpload =
        new FileUpload(
            file.getOriginalFilename(),
            file.getInputStream(),
            file.getContentType(),
            file.getSize());
    final StorageInfo storageInfo = this.fileManagementService.save(xAppName, fileUpload);
    return ResponseEntity.ok(this.storageInfoDtoMapper.toDto(storageInfo));
  }

  @Override
  @RequiredHeader("X-App-Name")
  public ResponseEntity<StorageInfoDto> findById(final UUID uploadId, final String xAppName) {
    final StorageInfo storageInfo = this.fileManagementService.findById(uploadId);
    return ResponseEntity.ok(this.storageInfoDtoMapper.toDto(storageInfo));
  }

  /**
   * Handles HTTP POST requests to download multiple files as a ZIP archive.
   *
   * @param downloadZipRequestDto DTO containing the list of file IDs to download
   * @param xAppName Name of the calling application, used for request tracking and ZIP filename
   * @return ResponseEntity containing a streaming ZIP file for download
   */
  @PostMapping(value = "/storage/download")
  @RequiredHeader("X-App-Name")
  public ResponseEntity<StreamingResponseBody> downloadZipByFileIds(
      @Parameter(name = "DownloadZipRequestDto", description = "", required = true)
          @Valid
          @RequestBody
          DownloadZipRequestDto downloadZipRequestDto,
      @Parameter(
              name = "X-App-Name",
              description = "Name of the application making the request. It is required",
              in = ParameterIn.HEADER)
          @RequestHeader(value = "X-App-Name")
          String xAppName) {
    final StreamingResponseBody streamingResponseBody =
        outputStream ->
            this.archiveUseCase.streamFilesFromBucket(
                xAppName, downloadZipRequestDto.getStorageInfoIds(), outputStream);

    return ResponseEntity.ok()
        .header(
            HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename="
                + URLEncoder.encode(
                    xAppName, StandardCharsets.UTF_8) // This give a name while downloading
                + ".zip")
        .body(streamingResponseBody);
  }

  @Override
  @RequiredHeader("X-App-Name")
  public ResponseEntity<InitiateUploadResponseDto> initiateUpload(
      final String fileName, final String xAppName) {
    final InitiateUploadResponse response =
        this.fileManagementService.initiateUpload(fileName, xAppName);
    final InitiateUploadResponseDto dto = this.initiateUploadResponseMapper.toDto(response);
    return ResponseEntity.ok(dto);
  }

  @SneakyThrows
  @Override
  @RequiredHeader("X-App-Name")
  public ResponseEntity<String> uploadChunk(
      final MultipartFile file, final String xAppName, final ChunkUploadRequestDto request) {
    final ChunkUploadRequest chunkUploadRequest =
        this.chunkUploadRequestMapper.toChunkUploadRequest(file.getInputStream(), request);
    final String etag = this.fileManagementService.uploadChunk(xAppName, chunkUploadRequest);
    return ResponseEntity.ok(etag);
  }

  @Override
  public ResponseEntity<StorageInfoDto> completeUpload(
      final CompleteRequestDto completeRequestDto, final String xAppName) {
    final CompleteRequest completeRequest =
        this.completeRequestMapper.toCompleteRequest(completeRequestDto);
    final StorageInfo storageInfo =
        this.fileManagementService.completeUpload(xAppName, completeRequest);
    final StorageInfoDto dto = this.storageInfoDtoMapper.toDto(storageInfo);
    return ResponseEntity.ok(dto);
  }
}
