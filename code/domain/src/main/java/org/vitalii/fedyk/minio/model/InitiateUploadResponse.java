package org.vitalii.fedyk.minio.model;

import java.util.UUID;
import lombok.Builder;

@Builder
public record InitiateUploadResponse(UUID storageInfoId, String fileName, String uploadId) {}
