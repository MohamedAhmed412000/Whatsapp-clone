package com.project.user.rest.inbound;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "UserContactCreationResource",
    description = "Schema to hold the user contact creation data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserContactCreationResource {
    @NotEmpty
    @Email
    @Schema(description = "The user email", example = "mohamed.ahmed@gmail.com")
    private String email;
    @Schema(description = "The contact fullName", example = "Mohamed Ahmed")
    private String fullName;
    @Schema(description = "Flag for favourite contacts")
    private Boolean isFavourite;
}
