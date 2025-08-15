package com.project.media.controllers;

import com.project.media.rest.outbound.BooleanResponse;
import com.project.media.rest.outbound.MediaReferenceListResponse;
import com.project.media.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<ResponseEntity<MediaReferenceListResponse>> uploadMedia(
        @RequestPart("files") Flux<FilePart> files,
        @RequestPart("filePath") String filePath,
        @RequestPart("entityId") String entityId
    ) {
        return files.flatMap(filePart -> mediaService.saveMedia(filePart, filePath, entityId))
            .collectList()
            .map(MediaReferenceListResponse::new)
            .map(ResponseEntity::ok);
    }

    @GetMapping("/list/{entityId}")
    public Mono<ResponseEntity<MediaReferenceListResponse>> getMediaListReferences(
        @PathVariable String entityId
    ) {
        return mediaService.getMediaList(entityId)
            .map(MediaReferenceListResponse::new)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/{reference}")
    public Mono<ResponseEntity<Resource>> getMediaView(@PathVariable String reference) {
        return Mono.just(mediaService.canGenerateMediaUrl())
            .flatMap(canGenerate -> {
                if (canGenerate) {
                    return mediaService.getMediaView(reference).map(mediaResourceDto ->
                        ResponseEntity.ok()
                            .contentType(mediaResourceDto.getMediaType())
                            .body(mediaResourceDto.getResource())
                    );
                } else {
                    return Mono.just(ResponseEntity.badRequest().body(null));
                }
            });
    }

    @DeleteMapping("/{reference}")
    public Mono<ResponseEntity<BooleanResponse>> deleteMedia(@PathVariable String reference) {
        return mediaService.deleteMedia(reference)
            .map(BooleanResponse::new)
            .map(ResponseEntity::ok);
    }
}