package org.vitalii.fedyk.minio.model;

public record FileUpload(String fileName, byte[] content, String contentType, long size) {}
