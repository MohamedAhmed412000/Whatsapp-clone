package com.project.story.clients;

import com.project.story.clients.dto.inbound.MediaUploadResource;
import com.project.story.clients.dto.outbound.MediaUploadResponse;
import org.springframework.http.ResponseEntity;

public interface MediaClient {
    ResponseEntity<MediaUploadResponse> saveMediaList(MediaUploadResource resource);
    void deleteMediaFile(String reference);
}
