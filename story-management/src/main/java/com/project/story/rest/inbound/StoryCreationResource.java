package com.project.story.rest.inbound;

import com.project.commons.validators.AtLeastOneFieldNotEmpty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Schema(
    name = "StoryCreationResource",
    description = "Schema to hold the story creation data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@AtLeastOneFieldNotEmpty
public class StoryCreationResource {
    @Schema(description = "The story string content", example = "Hello")
    private String content;
    @Schema(description = "The story media file content", format = "binary")
    private MultipartFile file;
}
