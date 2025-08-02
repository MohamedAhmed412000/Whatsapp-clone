package com.project.media.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Data
@AllArgsConstructor
public class MediaResourceDto {
    private Resource resource;
    private MediaType mediaType;
}
