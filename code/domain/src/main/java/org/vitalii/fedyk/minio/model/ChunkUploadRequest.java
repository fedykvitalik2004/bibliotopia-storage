package org.vitalii.fedyk.minio.model;

import java.io.InputStream;
import lombok.Builder;

@Builder
public record ChunkUploadRequest(
    InputStream inputStream, int chunkSize, String uploadId, int chunkNumber, String fileName) {}
