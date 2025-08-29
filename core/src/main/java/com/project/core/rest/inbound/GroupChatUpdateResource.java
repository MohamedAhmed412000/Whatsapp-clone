package com.project.core.rest.inbound;

import com.project.core.validators.AtLeastOneFieldNotEmpty;
import com.project.core.validators.ValidImageExtension;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Schema(
    name = "GroupChatUpdateResource",
    description = "Schema to hold the group chat update data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@AtLeastOneFieldNotEmpty
public class GroupChatUpdateResource {
    @Schema(description = "The group chat name", example = "Friends")
    private String name;
    @Schema(description = "The bio of the group chat", example = "...")
    private String description;
    @ValidImageExtension
    @Schema(description = "The group chat profile image (only image files allowed)", format = "binary")
    private MultipartFile chatImage;
}
