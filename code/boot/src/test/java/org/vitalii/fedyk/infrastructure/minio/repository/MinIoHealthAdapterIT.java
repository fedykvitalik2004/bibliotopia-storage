package org.vitalii.fedyk.infrastructure.minio.repository;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.vitalii.fedyk.minio.MinIoProperties;
import org.vitalii.fedyk.minio.repository.MinIoHealthAdapter;

@SpringBootTest(classes = {MinIoHealthAdapter.class, MinIoProperties.class})
@ActiveProfiles("test")
@EnableConfigurationProperties(MinIoProperties.class)
class MinIoHealthAdapterIT {
  @Autowired private MinIoHealthAdapter minIoHealthAdapter;

  @Test
  void healthCheck_shouldReturnTrue_whenMinIoIsLive() {
    // When
    final var actual = this.minIoHealthAdapter.healthCheck();

    // Then
    assertTrue(actual);
  }
}
