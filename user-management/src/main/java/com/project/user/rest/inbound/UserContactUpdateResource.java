package com.project.user.rest.inbound;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "UserContactUpdateResource",
    description = "Schema to hold the user contact update data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContactUpdateResource {
    @NotEmpty
    @Schema(description = "The contact fullName", example = "Mohamed Ahmed")
    private String fullName;
}
