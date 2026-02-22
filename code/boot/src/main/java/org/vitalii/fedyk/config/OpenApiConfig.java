package org.vitalii.fedyk.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

/** OpenAPI configuration for the Bibliotopia Storage service. */
@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Bibliotopia Storage API",
            version = "1.0.0",
            description = "API for uploading files to different storages",
            contact = @Contact(name = "Vitalii Fedyk", email = "fedykvitalik2004@gmail.com")))
public class OpenApiConfig {}
