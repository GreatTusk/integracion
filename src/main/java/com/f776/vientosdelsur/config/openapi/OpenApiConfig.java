package com.f776.vientosdelsur.config.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Seguridad")
                        .version("1.0")
                        .description("Endpoints para manejo de usuarios")
                        .contact(new Contact()
                                .name("Fernando")
                                .email("fern.belmar@duocuc.cl"))
                        .license(new License()
                                .name("Your License")
                                .url("https://your-license-url.com")));
    }

    @Bean
    public OpenAPI loginEndpointDocumentation() {
        return new OpenAPI()
                .path("/api/v1/auth/login", new io.swagger.v3.oas.models.PathItem()
                        .post(new io.swagger.v3.oas.models.Operation()
                                .operationId("login")
                                .summary("Login user and return JWT tokens")
                                .description("This endpoint logs in the user and returns JWT tokens for authentication")
                                .requestBody(new io.swagger.v3.oas.models.parameters.RequestBody()
                                        .content(new Content()
                                                .addMediaType(String.valueOf(MediaType.APPLICATION_JSON), new io.swagger.v3.oas.models.media.MediaType()
                                                        .schema(new io.swagger.v3.oas.models.media.ObjectSchema()
                                                                .addProperty("email", new io.swagger.v3.oas.models.media.StringSchema().example("user@example.com"))
                                                                .addProperty("password", new io.swagger.v3.oas.models.media.StringSchema().example("password")))))
                                )
                                .responses(new io.swagger.v3.oas.models.responses.ApiResponses()
                                        .addApiResponse("200", new ApiResponse().description("Successful login"))
                                        .addApiResponse("401", new ApiResponse().description("Unauthorized"))
                                )
                        )
                );
    }
}