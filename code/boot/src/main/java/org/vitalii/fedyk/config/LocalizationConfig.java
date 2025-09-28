package org.vitalii.fedyk.config;

import java.util.List;
import java.util.Locale;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

@Configuration
public class LocalizationConfig {
  private final Locale defaultLocale = Locale.ENGLISH;

  @Bean
  public AcceptHeaderLocaleResolver localeResolver() {
    final AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
    localeResolver.setDefaultLocale(defaultLocale);
    // It will resolve by language in this case
    localeResolver.setSupportedLocales(List.of(defaultLocale, new Locale("uk", "UA")));
    return localeResolver;
  }

  @Bean
  public MessageSource messageSource() {
    final ReloadableResourceBundleMessageSource messageSource =
        new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:messages");
    messageSource.setDefaultEncoding("UTF-8");
    messageSource.setFallbackToSystemLocale(false);
    return messageSource;
  }
}
