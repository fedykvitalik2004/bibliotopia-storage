package org.vitalii.fedyk.minio.model;

public record FileStorageResult(StorageLocation location, String accessUrl, long size, String contentType) {
}