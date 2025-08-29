package com.project.core.rest.inbound;

import com.project.core.validators.ValidImageExtension;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(
    name = "GroupChatCreationResource",
    description = "Schema to hold the group chat creation data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatCreationResource {
    @NotEmpty
    @Schema(description = "The group chat name", example = "Friends")
    private String name;
    @Schema(description = "The bio of the group chat", example = "...")
    private String description;
    @ValidImageExtension
    @Schema(description = "The group chat profile image (only image files allowed)", format = "binary")
    private MultipartFile file;
    @Size(min = 1, max = 20, message = "Not allowed number of receivers")
    @Schema(description = "List of user accounts Ids to be added in the group chat")
    private List<String> receiversIds;
}
