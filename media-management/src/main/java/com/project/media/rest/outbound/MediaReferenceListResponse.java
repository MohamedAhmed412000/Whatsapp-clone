package com.project.media.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Schema(
    name = "MediaReferenceListResponse",
    description = "Schema to hold the media list response"
)
@Data
@AllArgsConstructor
public class MediaReferenceListResponse {
    @Schema(description = "The entity media list references")
    private List<String> mediaReferencesList;
}
