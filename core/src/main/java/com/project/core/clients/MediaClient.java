package com.project.core.clients;

import com.project.core.clients.dto.inbound.MediaUploadResource;
import com.project.core.clients.dto.outbound.MediaUploadResponse;
import org.springframework.http.ResponseEntity;

public interface MediaClient {
    ResponseEntity<MediaUploadResponse> saveMediaList(MediaUploadResource resource);
    void deleteMediaFile(String reference);
}
