package com.project.core.rest.inbound;

import com.project.core.validators.ValidImageExtension;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatCreationResource {
    @NotEmpty
    private String name;
    private String description;
    @ValidImageExtension
    private MultipartFile file;
    @Size(min = 1, max = 20, message = "Not allowed number of receivers")
    private List<String> receiversIds;
}
