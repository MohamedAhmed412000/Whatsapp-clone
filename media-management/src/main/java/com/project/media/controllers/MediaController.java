package com.project.media.controllers;

import com.project.media.rest.inbound.MediaUploadResource;
import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.services.MediaService;
import lombok.RequiredArgsConstructor;
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
    public Mono<ResponseEntity<String>> uploadMedia(@RequestBody MediaUploadResource resource) {
        return mediaService.saveMedia(resource.getFile(), resource.getFilePath(), resource.getEntityId())
            .map(ResponseEntity::ok);
    }

    @GetMapping("/list/{entityId}")
    public Flux<MediaContentResponse> getMediaList(@PathVariable String entityId) {
        return mediaService.getMediaList(entityId);
    }

    @GetMapping("/{reference}")
    public Mono<ResponseEntity<MediaContentResponse>> getMedia(@PathVariable String reference) {
        return mediaService.getMedia(reference).map(ResponseEntity::ok);
    }

    @PostMapping("/url")
    public Mono<ResponseEntity<String>> getMediaUrl(@RequestBody String reference) {
        return Mono.just(mediaService.canGenerateMediaUrl())
            .flatMap(canGenerate -> {
                if (canGenerate) {
                    return mediaService.getMediaUrl(reference).map(ResponseEntity::ok);
                } else {
                    return Mono.just(ResponseEntity.badRequest()
                        .body("Media URL generation is not available for this storage type."));
                }
            });
    }

    @DeleteMapping("/{reference}")
    public Mono<ResponseEntity<Boolean>> deleteMedia(@PathVariable String reference) {
        return Mono.just(ResponseEntity.ok(mediaService.deleteMedia(reference)));
    }

}