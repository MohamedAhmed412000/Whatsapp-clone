package com.project.media.rest.inbound;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResource {
    @NotEmpty
    private MultipartFile file;
    private String entityId;
    @NotEmpty
    private String filePath;
}
