package com.project.core.clients.dto.inbound;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResource {
    @NotEmpty
    private List<MultipartFile> files;
    @NotEmpty
    private String entityId;
    @NotEmpty
    private String filePath;
}
