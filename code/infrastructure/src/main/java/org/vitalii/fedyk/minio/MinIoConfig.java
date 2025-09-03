package org.vitalii.fedyk.minio;

import java.net.URI;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

/**
 * Configuration class for MinIO client and presigner beans.
 * Provides Spring-managed {@link S3Client} and {@link S3Presigner}
 * instances configured to connect to a local MinIO server.
 */
@Configuration
@AllArgsConstructor
public class MinIoConfig {
  private MinIoProperties minIoProperties;

  /**
   * Creates and configures an {@link S3Client} for connecting to MinIO.
   *
   * @return a configured {@link S3Client} instance
   */
  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
            .endpointOverride(URI.create(minIoProperties.getEndpoint()))
            .region(Region.US_EAST_1)
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(minIoProperties.getAccessKeyId(), minIoProperties.getSecretKey())
                    )
            )
            .serviceConfiguration(
                    S3Configuration.builder()
                            .pathStyleAccessEnabled(true)
                            .build()
            )
            .build();
  }

  /**
   * Creates and configures an {@link S3Presigner} for generating pre-signed URLs.
   *
   * @return a configured {@link S3Presigner} instance
   */
  @Bean
  public S3Presigner s3Presigner() {
    return S3Presigner.builder()
            .endpointOverride(URI.create(minIoProperties.getEndpoint()))
            .credentialsProvider(
                    StaticCredentialsProvider.create(
                            AwsBasicCredentials.create(minIoProperties.getAccessKeyId(), minIoProperties.getSecretKey())
                    )
            )
            .region(Region.of(minIoProperties.getRegion()))
            .serviceConfiguration(
                    S3Configuration.builder()
                            .pathStyleAccessEnabled(true)
                            .build()
            )
            .build();
  }
}
