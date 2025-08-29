package com.project.media.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer wrapResponses() {
        return openAPI -> openAPI.getPaths().forEach((path, pathItem) ->
            pathItem.readOperations().forEach(operation ->
                operation.getResponses().forEach((status, apiResponse) -> {
                    Content content = apiResponse.getContent();
                    if (content != null && !status.equals("204")) {
                        content.forEach((mediaTypeKey, mediaTypeValue) -> {
                            if (mediaTypeKey.contains(MediaType.APPLICATION_JSON_VALUE) || !status.equals("200")) {
                                Schema<?> originalSchema = mediaTypeValue.getSchema();
                                if (originalSchema != null) {
                                    Schema<?> headersSchema = new ObjectSchema()
                                        .addProperty("requestId", new Schema<>()
                                            .type("string")
                                            .description("Unique request identifier")
                                            .example("string")
                                        )
                                        .addProperty("statusCode", new Schema<>()
                                            .type("string")
                                            .description("Status code of the response")
                                            .example("string")
                                        );

                                    Schema<?> wrapper = new ObjectSchema()
                                        .addProperty("headers", headersSchema)
                                        .addProperty("body", originalSchema);

                                    Content newContent = new Content();
                                    io.swagger.v3.oas.models.media.MediaType jsonMediaType =
                                        new io.swagger.v3.oas.models.media.MediaType();
                                    jsonMediaType.setSchema(wrapper);
                                    newContent.addMediaType(MediaType.APPLICATION_JSON_VALUE, jsonMediaType);

                                    apiResponse.setContent(newContent);
                                }
                            }
                        });
                    }
                })));
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("API Documentation"))
            .addSecurityItem(new SecurityRequirement().addList("keycloak"));
    }
}
