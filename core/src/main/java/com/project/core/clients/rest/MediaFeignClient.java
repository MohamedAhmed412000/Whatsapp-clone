package com.project.core.clients.rest;

import com.project.core.clients.MediaClient;
import com.project.core.clients.dto.inbound.MediaUploadResource;
import com.project.core.clients.dto.outbound.MediaUploadResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "media-management", dismiss404 = true, fallback = MediaClientFallback.class)
@ConditionalOnProperty(name = "media.service.grpc.enabled", havingValue = "false")
public interface MediaFeignClient extends MediaClient {
    @PostMapping(value = "/api/v1/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MediaUploadResponse> saveMediaList(@ModelAttribute MediaUploadResource resource);

    @DeleteMapping(value = "/api/v1/media/{reference}")
    void deleteMediaFile(@PathVariable("reference") String reference);
}
