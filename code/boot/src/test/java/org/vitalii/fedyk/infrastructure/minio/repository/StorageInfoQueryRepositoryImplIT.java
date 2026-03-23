package org.vitalii.fedyk.infrastructure.minio.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.vitalii.fedyk.minio.model.StorageBucketStats;
import org.vitalii.fedyk.minio.repository.StorageInfoQueryRepositoryImpl;

import java.time.OffsetDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest(classes = StorageInfoQueryRepositoryImpl.class)
@EnableAutoConfiguration
@ActiveProfiles("test")
class StorageInfoQueryRepositoryImplIT {
  @Autowired private StorageInfoQueryRepositoryImpl storageInfoQueryRepository;

  @Test
  void shouldReturnCorrectBucketStats() {
    // Given
    final var expected = List.of(
            StorageBucketStats.builder()
                    .bucketName("u1v2w3x4")
                    .totalCount(9)
                    .completedCount(6)
                    .latestCreatedAt(OffsetDateTime.parse("2026-03-21T16:25:44Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("m3n4o5p6")
                    .totalCount(9)
                    .completedCount(5)
                    .latestCreatedAt(OffsetDateTime.parse("2026-03-16T04:52:40Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("i9j0k1l2")
                    .totalCount(12)
                    .completedCount(8)
                    .latestCreatedAt(OffsetDateTime.parse("2026-03-12T06:45:58Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("c9d0e1f2")
                    .totalCount(13)
                    .completedCount(7)
                    .latestCreatedAt(OffsetDateTime.parse("2026-03-11T18:05:05Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("q7r8s9t0")
                    .totalCount(16)
                    .completedCount(8)
                    .latestCreatedAt(OffsetDateTime.parse("2026-03-11T06:45:08Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("k7l8m9n0")
                    .totalCount(8)
                    .completedCount(5)
                    .latestCreatedAt(OffsetDateTime.parse("2026-02-25T11:00:26Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("g3h4i5j6")
                    .totalCount(8)
                    .completedCount(4)
                    .latestCreatedAt(OffsetDateTime.parse("2026-02-11T10:55:59Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("y5z6a7b8")
                    .totalCount(9)
                    .completedCount(4)
                    .latestCreatedAt(OffsetDateTime.parse("2026-02-07T05:16:52Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("a1b2c3d4")
                    .totalCount(11)
                    .completedCount(4)
                    .latestCreatedAt(OffsetDateTime.parse("2026-02-01T14:49:14Z"))
                    .build(),
            StorageBucketStats.builder()
                    .bucketName("e5f6g7h8")
                    .totalCount(5)
                    .completedCount(3)
                    .latestCreatedAt(OffsetDateTime.parse("2025-11-30T16:45:50Z"))
                    .build()
    );

    // When
    final var actual = this.storageInfoQueryRepository.getBucketsWithStats();

    // Then
    assertIterableEquals(expected, actual);
  }
}
