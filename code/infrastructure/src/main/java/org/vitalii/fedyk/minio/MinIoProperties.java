package org.vitalii.fedyk.minio;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "minio")
@Data
public class MinIoProperties {
  private String accessKeyId;
  private String secretKey;
  private String endpoint;
}
