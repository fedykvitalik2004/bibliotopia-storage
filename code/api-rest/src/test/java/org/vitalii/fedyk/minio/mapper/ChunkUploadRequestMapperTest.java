package org.vitalii.fedyk.minio.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import org.junit.jupiter.api.Test;
import org.openapitools.model.ChunkUploadRequestDto;
import org.vitalii.fedyk.minio.model.ChunkUploadRequest;

class ChunkUploadRequestMapperTest {
  private final ChunkUploadRequestMapper chunkUploadRequestMapper =
      new ChunkUploadRequestMapperImpl();

  @Test
  void shouldMapDtoAndInputStreamToDomainModel() {
    // Given
    final var request = new ChunkUploadRequestDto();
    request.setChunkSize(3);
    request.setUploadId("uploadId");
    request.setChunkNumber(2);
    request.setFileName("filename");

    final var inputStream = new ByteArrayInputStream("Hello".getBytes());

    final var expected =
        ChunkUploadRequest.builder()
            .fileName(request.getFileName())
            .chunkNumber(request.getChunkNumber())
            .uploadId(request.getUploadId())
            .chunkSize(request.getChunkSize())
            .inputStream(inputStream)
            .build();

    // When
    final var actual = this.chunkUploadRequestMapper.toChunkUploadRequest(inputStream, request);

    // Then
    assertEquals(expected, actual);
  }
}
