package org.vitalii.fedyk.minio.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record FileStorageResult(
    StorageLocation location, String accessUrl, long size, String contentType) {}
