package com.project.core.rest.inbound;

import com.project.core.domain.dto.MobileNumber;
import com.project.core.validators.AtLeastOneFieldNotEmpty;
import com.project.core.validators.ValidImageExtension;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AtLeastOneFieldNotEmpty
public class UserUpdateResource {
    private String firstName;
    private String lastName;
    private MobileNumber mobileNumber;
    @ValidImageExtension
    private MultipartFile profilePicture;
}
