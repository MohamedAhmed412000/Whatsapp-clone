package com.project.core.clients;

import com.project.core.clients.dto.inbound.MediaUploadResource;
import com.project.core.clients.dto.outbound.MediaUploadResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "media-management")
public interface MediaFeignClient {
    @PostMapping(value = "/api/v1/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MediaUploadResponse> saveMediaList(@ModelAttribute MediaUploadResource resource);

    @DeleteMapping(value = "/api/v1/media/{reference}")
    void deleteMediaFile(@PathVariable("reference") String reference);
}
