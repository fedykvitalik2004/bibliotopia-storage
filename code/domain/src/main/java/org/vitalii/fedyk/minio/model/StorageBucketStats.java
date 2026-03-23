package org.vitalii.fedyk.minio.model;

import java.time.OffsetDateTime;
import lombok.Builder;

@Builder
public record StorageBucketStats(
    String bucketName, int totalCount, int completedCount, OffsetDateTime latestCreatedAt) {}
