package com.project.whatsapp.rest.inbound;

import com.project.whatsapp.domain.dto.MobileNumber;
import com.project.whatsapp.validators.AtLeastOneFieldNotEmpty;
import com.project.whatsapp.validators.ValidImageExtension;
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
