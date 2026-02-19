package org.vitalii.fedyk.minio.mapper;

import java.io.InputStream;
import org.mapstruct.Mapper;
import org.openapitools.model.ChunkUploadRequestDto;
import org.vitalii.fedyk.minio.model.ChunkUploadRequest;

/**
 * Mapper interface responsible for transforming API-layer data into Domain-layer models for chunked
 * file uploads.
 */
@Mapper(componentModel = "spring")
public interface ChunkUploadRequestMapper {
  ChunkUploadRequest toChunkUploadRequest(
      InputStream inputStream, ChunkUploadRequestDto chunkUploadRequest);
}
