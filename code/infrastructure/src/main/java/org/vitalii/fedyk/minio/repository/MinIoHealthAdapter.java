package org.vitalii.fedyk.minio.repository;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.vitalii.fedyk.common.HttpClientBase;
import org.vitalii.fedyk.minio.MinIoProperties;

/**
 * Adapter implementation of {@link MinIoHealthRepository} that checks the health of a MinIO service
 * via HTTP.
 */
@Repository
@Slf4j
public class MinIoHealthAdapter extends HttpClientBase implements MinIoHealthRepository {

  /**
   * Constructs a new {@code MinIoHealthAdapter} using the specified MinIO properties.
   *
   * @param properties MinIO configuration properties (URL, etc.)
   */
  @Autowired
  public MinIoHealthAdapter(@Qualifier("minIoProperties") final MinIoProperties properties) {
    super(
        properties.getUrlInternal(),
        HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(5)).build());
  }

  @Override
  public boolean healthCheck() {
    final HttpRequest request =
        HttpRequest.newBuilder()
            .header("Content-Type", "application/json")
            .uri(URI.create(this.baseurl).resolve("/minio/health/live"))
            .GET()
            .build();

    try {
      final HttpResponse<Void> response =
          client.send(request, HttpResponse.BodyHandlers.discarding());
      log.debug("MinIO health check failed with status: {}", response.statusCode());
      return response.statusCode() == HttpStatus.OK.value();
    } catch (Exception e) {
      log.error("MinIO health check failed: {}", e.getMessage());
      return false;
    }
  }
}
