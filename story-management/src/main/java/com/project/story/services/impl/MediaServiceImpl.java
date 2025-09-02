package com.project.story.services.impl;

import com.project.story.clients.MediaClient;
import com.project.story.clients.dto.inbound.MediaUploadResource;
import com.project.story.clients.dto.outbound.MediaUploadResponse;
import com.project.story.constants.Application;
import com.project.story.exceptions.MediaUploadException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl {

    private final MediaClient mediaClient;

    public String saveUserStory(MultipartFile file, String userId) {
        ResponseEntity<MediaUploadResponse> response = mediaClient.saveMediaList(
            MediaUploadResource.builder()
                .files(List.of(file))
                .entityId(generateMediaId(Application.STORY_MEDIA_PREFIX, userId))
                .filePath(userId)
                .build()
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getMediaReferencesList().get(0);
        }
        throw new MediaUploadException("Error saving story media");
    }

    public String updateUserStory(String reference, MultipartFile file, String userId) {
        if (reference != null)
            mediaClient.deleteMediaFile(reference);
        return saveUserStory(file, userId);
    }

    private String generateMediaId(@NonNull String prefix, @NonNull String entityId) {
        return prefix + entityId;
    }
}
