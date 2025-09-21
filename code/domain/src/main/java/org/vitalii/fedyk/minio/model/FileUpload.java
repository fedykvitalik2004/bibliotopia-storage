package org.vitalii.fedyk.minio.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FileUpload {
  private String fileName;
  private byte[] content;
  private String contentType;
  private long size;
}