package com.project.user.rest.inbound;

import com.project.commons.validators.AtLeastOneFieldNotEmpty;
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
@AtLeastOneFieldNotEmpty
public class UserContactUpdateResource {
    @Schema(description = "The contact firstname", example = "Mohamed")
    private String firstname;
    @Schema(description = "The contact lastname", example = "Ahmed")
    private String lastname;
}
