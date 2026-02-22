package org.vitalii.fedyk.minio.repository;

/**
 * Repository interface for interacting with MinIO.
 *
 * <p>Provides methods to perform admin operations.
 */
public interface MinIoHealthRepository {

  /**
   * Checks the health status of the MinIO server.
   *
   * @return {@code true} if the MinIO server is healthy and reachable, {@code false} if the server
   *     is unavailable or unhealthy.
   */
  boolean healthCheck();
}
