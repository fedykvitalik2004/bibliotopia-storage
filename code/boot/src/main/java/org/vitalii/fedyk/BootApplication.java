package org.vitalii.fedyk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.vitalii.fedyk.minio.MinIoProperties;

@SpringBootApplication
@EnableConfigurationProperties(MinIoProperties.class)
public class BootApplication {

  public static void main(String[] args) {
    SpringApplication.run(BootApplication.class, args);
  }
}
