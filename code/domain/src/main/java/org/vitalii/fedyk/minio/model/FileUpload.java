package org.vitalii.fedyk.minio.model;

import java.io.InputStream;

public record FileUpload(String fileName, InputStream content, String contentType, long size) {}
