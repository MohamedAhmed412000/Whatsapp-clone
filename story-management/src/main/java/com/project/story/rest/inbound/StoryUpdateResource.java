package com.project.story.rest.inbound;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "StoryUpdateResource",
    description = "Schema to hold the story update data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoryUpdateResource {
    @NotEmpty
    @Schema(description = "The story string content", example = "Hello")
    private String content;
}
