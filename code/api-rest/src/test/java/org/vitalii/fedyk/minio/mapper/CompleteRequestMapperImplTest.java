package org.vitalii.fedyk.minio.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.openapitools.model.CompleteRequestDto;
import org.openapitools.model.PartEtagDto;
import org.vitalii.fedyk.minio.model.CompleteRequest;

class CompleteRequestMapperImplTest {
  private final CompleteRequestMapper completeRequestMapper = new CompleteRequestMapperImpl();

  @Test
  void shouldMapCompleteRequestDtoToDomainModel() {
    // Given
    final var dto = new CompleteRequestDto();
    dto.setFileName("video.mp4");
    dto.setUploadId("multipart-id-xyz");

    final var part1 = new PartEtagDto();
    part1.setPartNumber(1);
    part1.setEtag("etag-checksum-1");

    final var part2 = new PartEtagDto();
    part2.setPartNumber(2);
    part2.setEtag("etag-checksum-2");

    dto.setParts(List.of(part1, part2));

    final var expectedParts =
        List.of(
            CompleteRequest.Part.builder().partNumber(1).etag("etag-checksum-1").build(),
            CompleteRequest.Part.builder().partNumber(2).etag("etag-checksum-2").build());

    // When
    final var result = this.completeRequestMapper.toCompleteRequest(dto);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.fileName()).isEqualTo("video.mp4");
    assertThat(result.uploadId()).isEqualTo("multipart-id-xyz");

    assertEquals(expectedParts, result.parts());
  }
}
