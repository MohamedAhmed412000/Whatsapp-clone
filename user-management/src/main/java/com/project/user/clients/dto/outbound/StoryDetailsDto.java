package com.project.user.clients.dto.outbound;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.project.user.domain.enums.StoryTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Schema(
    name = "StoryDetailsResponse",
    description = "Schema to hold the story details response"
)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StoryDetailsDto {
    @Schema(description = "The story id", example = "1755250373935")
    private Long id;
    @Schema(description = "The story string content", example = "Hello")
    private String content;
    @Schema(description = "The story media reference", example = "407dd10ad78743e49d0b58e7")
    private String mediaReference;
    @Schema(description = "The story type", allowableValues = {
        "TEXT", "MEDIA"
    })
    private StoryTypeEnum storyType;
    @Schema(description = "The story creation date")
    private LocalDateTime createdAt;
}
