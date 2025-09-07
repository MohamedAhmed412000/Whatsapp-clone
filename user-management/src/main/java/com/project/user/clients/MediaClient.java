package com.project.user.clients;

import com.project.user.clients.dto.inbound.MediaUploadResource;
import com.project.user.clients.dto.outbound.MediaUploadResponse;
import org.springframework.http.ResponseEntity;

public interface MediaClient {
    ResponseEntity<MediaUploadResponse> saveMediaList(MediaUploadResource resource);
    void deleteMediaFile(String reference);
}
