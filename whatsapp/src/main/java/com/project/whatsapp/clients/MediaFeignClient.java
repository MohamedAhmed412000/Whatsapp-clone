package com.project.whatsapp.clients;

import com.project.whatsapp.clients.dto.inbound.MediaUploadResource;
import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "media-management")
public interface MediaFeignClient {

    @PostMapping(value = "/api/v1/media", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    ResponseEntity<String> saveMedia(@RequestBody MediaUploadResource resource);

    @GetMapping(value = "/api/v1/media/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<MediaContentResponse> getMediaContent(
        @PathVariable("entityId") String entityId
    );

}
