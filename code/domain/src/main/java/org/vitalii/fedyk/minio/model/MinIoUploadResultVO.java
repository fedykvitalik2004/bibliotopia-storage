package org.vitalii.fedyk.minio.model;

import lombok.Builder;

@Builder
public record MinIoUploadResultVO(String bucketName, String objectName, String url, long size, String contentType) {
}
