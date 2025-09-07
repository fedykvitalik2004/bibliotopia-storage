package org.vitalii.fedyk.minio.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder(toBuilder = true)
public class MinIoObjectInfo {
  private UUID id;
  private String bucketName;
  private String objectName;
  private long size;
  private String contentType;
  private OffsetDateTime createdAt;
  private String url;
}
