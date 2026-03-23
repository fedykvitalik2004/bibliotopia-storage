package org.vitalii.fedyk.minio.repository;

import java.util.List;
import org.vitalii.fedyk.minio.model.StorageBucketStats;

/**
 * Retrieves aggregate statistics for all storage buckets using a native SQL query. This
 * implementation bypasses the JPA provider to execute a high-performance aggregation directly on
 * the database.
 */
public interface StorageInfoQueryRepository {
  List<StorageBucketStats> getBucketsWithStats();
}
