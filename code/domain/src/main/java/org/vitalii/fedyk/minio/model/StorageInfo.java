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
public class StorageInfo {
  private UUID id;
  private String bucketName;
  private String objectName;
  private boolean complete;
  private OffsetDateTime createdAt;
  private String url;
}
