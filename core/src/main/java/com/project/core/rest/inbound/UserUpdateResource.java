package com.project.core.rest.inbound;

import com.project.core.domain.dto.MobileNumber;
import com.project.core.validators.AtLeastOneFieldNotEmpty;
import com.project.core.validators.ValidImageExtension;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Schema(
    name = "UserUpdateResource",
    description = "Schema to hold the user details update data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@AtLeastOneFieldNotEmpty
public class UserUpdateResource {
    @Schema(description = "The user firstname", example = "Mohamed")
    private String firstName;
    @Schema(description = "The user lastname", example = "Ahmed")
    private String lastName;
    private MobileNumber mobileNumber;
    @ValidImageExtension
    @Schema(description = "The user profile image (only image files allowed)", format = "binary")
    private MultipartFile profilePicture;
}
