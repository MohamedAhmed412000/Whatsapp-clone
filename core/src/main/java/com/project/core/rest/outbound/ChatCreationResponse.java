package com.project.core.rest.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Schema(
    name = "ChatCreationResponse",
    description = "Schema to hold the chat creation response"
)
@Data
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCreationResponse {
    @Schema(description = "The chat id", example = "c88438c5-260b-4e70-811a-0ceaead085e5")
    private String id;
    @Schema(description = "The chat profile image reference")
    private String chatImageReference;
}
