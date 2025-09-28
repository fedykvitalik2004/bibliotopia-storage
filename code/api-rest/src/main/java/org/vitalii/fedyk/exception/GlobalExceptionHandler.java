package org.vitalii.fedyk.exception;

import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.vitalii.fedyk.common.exception.LocalizedException;

@RestControllerAdvice
@AllArgsConstructor
public class GlobalExceptionHandler {
  private MessageSource messageSource;

  @ExceptionHandler(LocalizedException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public ErrorResponse handleLocalizedException(
      final LocalizedException localizedException, final Locale locale) {
    final String localizedMessage =
        resolveMessage(
            localizedException.getMessage(), localizedException.getMessageArguments(), locale);
    return ErrorResponse.builder().message(localizedMessage).locale(locale).build();
  }

  private String resolveMessage(
      final String message, final Object[] messageArguments, final Locale locale) {
    return messageSource.getMessage(message, messageArguments, locale);
  }

  @AllArgsConstructor
  @Builder
  @Getter
  public static class ErrorResponse {
    private String message;
    private Locale locale;
  }
}
