package org.vitalii.fedyk.minio.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FileUpload {
  private String fileName;
  private byte[] content;
  private String contentType;
  private long size;
}