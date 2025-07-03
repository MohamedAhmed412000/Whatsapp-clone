package com.project.media.controllers;

import com.project.media.rest.inbound.MediaContentResource;
import com.project.media.rest.inbound.MediaUploadResource;
import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.rest.outbound.MediaListResponse;
import com.project.media.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<String>> addMedia(@RequestBody MediaUploadResource resource) {
        return mediaService.saveMedia(resource.getFile(), resource.getFilePath(), resource.getEntityId())
            .map(ResponseEntity::ok);
    }

    @GetMapping("/{entityId}")
    public Flux<MediaContentResponse> getMedia(@PathVariable String entityId) {
        return mediaService.getMediaContent(entityId);
    }

    @PostMapping("/list")
    public Flux<Pair<String, MediaListResponse>> getMediaList(
        @RequestBody MediaContentResource resource
    ) {
        return mediaService.getMediaList(resource);
    }

}