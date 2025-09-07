package com.project.user.clients.rest;

import com.project.user.clients.dto.inbound.MediaUploadResource;
import com.project.user.clients.dto.outbound.MediaUploadResponse;
import com.project.user.exceptions.DeleteActionNotAllowedException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "media.service.grpc.enabled", havingValue = "false")
public class MediaClientFallback implements MediaFeignClient {
    @Override
    public ResponseEntity<MediaUploadResponse> saveMediaList(MediaUploadResource resource) {
        return ResponseEntity.internalServerError().body(null);
    }

    @Override
    public void deleteMediaFile(String reference) {
        throw new DeleteActionNotAllowedException("Cannot delete media file");
    }
}
