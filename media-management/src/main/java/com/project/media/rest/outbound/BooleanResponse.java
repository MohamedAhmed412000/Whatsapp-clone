package com.project.media.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "BooleanResponse",
    description = "Schema to hold the boolean response"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BooleanResponse {
    @Schema(description = "Flag to describe if the action is done")
    private Boolean isDone;
}
