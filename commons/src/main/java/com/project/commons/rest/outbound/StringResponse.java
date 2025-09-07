package com.project.commons.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "StringResponse",
    description = "Schema to hold the string response"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StringResponse {
    @Schema(description = "Response string content")
    private String content;
}
