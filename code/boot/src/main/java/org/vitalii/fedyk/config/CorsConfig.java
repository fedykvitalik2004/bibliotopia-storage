package org.vitalii.fedyk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Configuration class for setting up Cross-Origin Resource Sharing (CORS) rules. */
@Configuration
public class CorsConfig {
  /** Creates a {@link WebMvcConfigurer} bean to configure global CORS settings. */
  @Bean
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurer() {
      @Override
      public void addCorsMappings(final CorsRegistry registry) {
        registry
            .addMapping("/**")
            .allowedOrigins("*")
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS");
      }
    };
  }
}
