package org.vitalii.fedyk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.vitalii.fedyk.minio.controller.LoggingInterceptor;

/** Configuration class for Spring MVC. */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(final InterceptorRegistry registry) {
    registry.addInterceptor(new LoggingInterceptor());
  }
}
