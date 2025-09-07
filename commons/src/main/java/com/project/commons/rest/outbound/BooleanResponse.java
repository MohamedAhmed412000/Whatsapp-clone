package com.project.commons.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "BooleanResponse",
    description = "Schema to hold the boolean response"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BooleanResponse {
    @Schema(description = "Flag to describe if the action is done")
    private boolean isDone;
}
