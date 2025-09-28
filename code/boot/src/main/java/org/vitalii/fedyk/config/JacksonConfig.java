package org.vitalii.fedyk.config;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.TimeZone;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** Configures Jackson for the application. */
@Configuration
public class JacksonConfig {

  /**
   * Customizes the Jackson ObjectMapper used by Spring Boot.
   *
   * @return a customizer that sets UTC time zone and registers JavaTimeModule
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer customizer() {
    return builder -> builder.timeZone(TimeZone.getTimeZone("UTC")).modules(new JavaTimeModule());
  }
}
