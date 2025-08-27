package com.project.core.rest.inbound;

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
public class GroupChatUpdateResource {
    private String name;
    private String description;
    @ValidImageExtension
    private MultipartFile chatImage;
}
