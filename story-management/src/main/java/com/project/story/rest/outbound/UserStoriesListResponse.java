package com.project.story.rest.outbound;

import com.project.story.domain.dto.StoryDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(
    name = "UserStoriesListResponse",
    description = "Schema to hold the story details response data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoriesListResponse {
    private List<StoryDetailsDto> storiesList;
}
