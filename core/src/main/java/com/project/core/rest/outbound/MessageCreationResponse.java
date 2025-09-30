package com.project.core.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Schema(
    name = "MessageCreationResponse",
    description = "Schema to hold the message creation response"
)
@Data
@AllArgsConstructor
@Builder
public class MessageCreationResponse {
    @Schema(description = "The message id", example = "1755250373935")
    private Long id;
    @Schema(description = "The message media list references")
    private List<String> mediaListReferences;
}
