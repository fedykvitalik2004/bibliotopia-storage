package org.vitalii.fedyk.minio.repository;

import jakarta.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.minio.model.StorageBucketStats;

@Repository
@AllArgsConstructor
public class StorageInfoQueryRepositoryImpl implements StorageInfoQueryRepository {
  private final EntityManager entityManager;

  @Override
  public List<StorageBucketStats> getBucketsWithStats() {
    final Session session = this.entityManager.unwrap(Session.class);
    return session.doReturningWork(
        connection -> {
          connection.setReadOnly(true);
          final String sql =
              """
                              SELECT bucket_name,
                                     COUNT(*) AS count,
                                     SUM(CASE WHEN complete THEN 1 ELSE 0 END) AS completed_count,
                                     MAX(created_at) AS latest_created_at
                              FROM storage_infos
                              GROUP BY bucket_name
                              ORDER BY latest_created_at DESC
                              """;

          final List<StorageBucketStats> result = new ArrayList<>();

          try (final PreparedStatement preparedStatement = connection.prepareStatement(sql);
              final ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
              result.add(
                  StorageBucketStats.builder()
                      .bucketName(resultSet.getString("bucket_name"))
                      .totalCount(resultSet.getInt("count"))
                      .completedCount(resultSet.getInt("completed_count"))
                      .latestCreatedAt(
                          resultSet.getObject("latest_created_at", OffsetDateTime.class))
                      .build());
            }
          }
          return result;
        });
  }
}
