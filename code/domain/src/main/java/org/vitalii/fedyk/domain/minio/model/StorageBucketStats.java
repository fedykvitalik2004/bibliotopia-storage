package org.vitalii.fedyk.domain.minio.model;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record StorageBucketStats(
    String bucketName, int totalCount, int completedCount, OffsetDateTime latestCreatedAt) {}
