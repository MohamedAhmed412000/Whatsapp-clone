package com.project.user.clients.dto.inbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "UserStoriesListResource",
    description = "Schema to hold the story details resource data"
)
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStoriesListResource {
    private String userId;
}
