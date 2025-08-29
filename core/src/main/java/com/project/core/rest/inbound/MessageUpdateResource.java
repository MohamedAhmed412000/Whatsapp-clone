package com.project.core.rest.inbound;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "MessageUpdateResource",
    description = "Schema to hold the message update data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageUpdateResource {
    @NotEmpty
    @Schema(description = "The message string content", example = "Hello")
    private String content;
}
