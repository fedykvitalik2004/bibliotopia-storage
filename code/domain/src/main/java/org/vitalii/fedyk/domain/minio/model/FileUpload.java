package org.vitalii.fedyk.domain.minio.model;

import java.io.InputStream;

public record FileUpload(String fileName, InputStream content, String contentType, long size) {}
