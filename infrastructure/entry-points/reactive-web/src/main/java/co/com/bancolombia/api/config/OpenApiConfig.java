package co.com.bancolombia.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Pragma Template API",
                version = "1.0.0",
                description = "API for Pragma Template Application",
                contact = @Contact(
                        name = "Pragma Team",
                        email = "support@pragma.co",
                        url = "https://pragma.co"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "https://www.apache.org/licenses/LICENSE-2.0"
                )
        )
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    public OpenAPI api() {
        Schema<?> apiError = new ObjectSchema().properties(Map.of(
                "timestamp", new StringSchema().format("date-time").example("2025-08-23T16:23:16.107+00:00"),
                "path", new StringSchema().example("/path"),
                "status", new StringSchema().example("status"),
                "error", new StringSchema().example("error"),
                "requestId", new StringSchema().example("4a8e2e37-1"),
                "message", new StringSchema().example("No static resource.")
        ));
        Paths paths = new Paths();
        paths.addPathItem("/api/v1/auth", new PathItem());

        return new OpenAPI()
                .components(new Components()
                        .addSchemas("ApiError", apiError)
                        .addSecuritySchemes("bearerAuth",
                                new io.swagger.v3.oas.models.security.SecurityScheme()
                                        .type(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .in(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER)
                                        .name("Authorization")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .paths(paths);
    }
}