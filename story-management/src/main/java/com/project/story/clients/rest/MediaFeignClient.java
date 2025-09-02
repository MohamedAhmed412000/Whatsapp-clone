package com.project.story.clients.rest;

import com.project.story.clients.MediaClient;
import com.project.story.clients.dto.inbound.MediaUploadResource;
import com.project.story.clients.dto.outbound.MediaUploadResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "media-management")
@ConditionalOnProperty(name = "media.service.grpc.enabled", havingValue = "false")
public interface MediaFeignClient extends MediaClient {
    @PostMapping(value = "/api/v1/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MediaUploadResponse> saveMediaList(@ModelAttribute MediaUploadResource resource);

    @DeleteMapping(value = "/api/v1/media/{reference}")
    void deleteMediaFile(@PathVariable("reference") String reference);
}
