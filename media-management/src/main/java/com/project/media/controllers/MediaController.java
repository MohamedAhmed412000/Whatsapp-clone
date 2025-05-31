package com.project.media.controllers;

import com.project.media.rest.inbound.MediaContentResource;
import com.project.media.rest.inbound.MediaUploadResource;
import com.project.media.rest.outbound.MediaListResponse;
import com.project.media.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addMedia(@RequestBody MediaUploadResource resource) {
        String fileReference = mediaService.saveMedia(resource.getFile(), resource.getFilePath(),
            resource.getEntityId());
        return ResponseEntity.ok(fileReference);
    }

    @PostMapping("/list")
    public ResponseEntity<Map<String, MediaListResponse>> getMediaList(
        @RequestBody MediaContentResource resource
    ) {
        return ResponseEntity.ok(mediaService.getMediaList(resource));
    }

}
