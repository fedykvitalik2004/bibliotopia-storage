package org.vitalii.fedyk.minio.model;

import java.util.List;
import lombok.Builder;

@Builder
public record CompleteRequest(String fileName, String uploadId, List<Part> parts) {

  @Builder
  public record Part(int partNumber, String etag) {}
}
