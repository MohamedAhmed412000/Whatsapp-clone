package com.project.core.config;

import com.project.commons.enums.GeneralCodesEnum;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenApiCustomizer wrapResponses() {
        return openAPI -> openAPI.getPaths().forEach((path, pathItem) ->
            pathItem.readOperations().forEach(operation ->
                operation.getResponses().forEach((status, apiResponse) -> {
                    Content content = apiResponse.getContent();
                    if (content != null) {
                        content.forEach((mediaTypeKey, mediaTypeValue) -> {
                            Schema<?> originalSchema = mediaTypeValue.getSchema();
                            if (originalSchema != null) {
                                Schema<?> headersSchema = new ObjectSchema()
                                    .addProperty("requestId", new Schema<>()
                                        .type("string")
                                        .description("Unique request identifier")
                                        .example(UUID.randomUUID().toString())
                                    )
                                    .addProperty("statusCode", new Schema<>()
                                        .type("string")
                                        .description("Status code of the response")
                                        .example(GeneralCodesEnum.SUCCESS.getApplicationCode())
                                    );

                                Schema<?> wrapper = new ObjectSchema()
                                    .addProperty("headers", headersSchema)
                                    .addProperty("body", originalSchema);
                                mediaTypeValue.setSchema(wrapper);
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
