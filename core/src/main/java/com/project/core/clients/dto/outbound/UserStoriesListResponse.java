package com.project.core.clients.dto.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(
    name = "UserStoriesListResponse",
    description = "Schema to hold the stories list response data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoriesListResponse {
    @Schema(description = "The stories list")
    private List<StoryDetailsDto> storiesList;
}
