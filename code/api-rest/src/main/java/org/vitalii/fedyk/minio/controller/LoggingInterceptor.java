package org.vitalii.fedyk.minio.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Intercepts incoming requests before they are handled by a controller method.
 * This method checks if the target controller method is annotated with {@link RequiredHeader}.
 * If the annotation is present, it validates that the HTTP request contains the specified
 * header. If the header is missing, it sends an {@code HTTP 401 Unauthorized} error
 * response and prevents the request from proceeding. This ensures that methods marked
 * with the annotation are only accessible with the correct security header.
 */
public class LoggingInterceptor implements HandlerInterceptor {
  @Override
  public boolean preHandle(@NonNull final HttpServletRequest request,
                           @NonNull final HttpServletResponse response,
                           @NonNull final Object handler) throws Exception {
    if (handler instanceof HandlerMethod method) {
      final RequiredHeader annotation = method.getMethodAnnotation(RequiredHeader.class);
      if (annotation != null) {
        final String header = request.getHeader(annotation.value());
        if (header == null) {
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing app header");
          return false;
        }
      }
    }
    return true;
  }
}
