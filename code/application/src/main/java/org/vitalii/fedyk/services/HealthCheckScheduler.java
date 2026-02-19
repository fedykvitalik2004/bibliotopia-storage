package org.vitalii.fedyk.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.vitalii.fedyk.minio.repository.MinIoHealthRepository;

/** Scheduler responsible for periodically checking the health of the services. */
@Component
@Slf4j
@AllArgsConstructor
public class HealthCheckScheduler {
  private final MinIoHealthRepository minIoHealthRepository;

  /** Performs a scheduled health check of the MinIO service. */
  @Scheduled(cron = "0 0/1 * * * ?")
  public void performMinIoHealthCheck() {
    log.info("Starting scheduled MinIO health check");
    if (!minIoHealthRepository.healthCheck()) {
      log.error("MinIO health check FAILED");
    }
  }
}
