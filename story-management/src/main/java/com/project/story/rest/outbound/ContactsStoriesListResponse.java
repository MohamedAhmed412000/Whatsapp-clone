package com.project.story.rest.outbound;

import com.project.story.domain.dto.StoryDetailsDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Schema(
    name = "ContactsStoriesListResponse",
    description = "Schema to hold the contacts story details response data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactsStoriesListResponse {
    private Map<String, List<StoryDetailsDto>> contactsStoriesList;
}
