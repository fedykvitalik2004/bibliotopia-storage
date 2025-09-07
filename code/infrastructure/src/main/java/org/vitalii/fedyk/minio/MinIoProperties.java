package org.vitalii.fedyk.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for connecting to a MinIO server.
 * <p>
 * These properties are automatically bound from the application's
 * configuration (e.g., application.yml or application.properties)
 * using the prefix {@code minio}.
 * </p>
 */
@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinIoProperties {
  private String accessKeyId;
  private String secretKey;
  private String endpoint;
}
