package com.project.user.services.impl;

import com.project.user.clients.MediaClient;
import com.project.user.clients.dto.inbound.MediaUploadResource;
import com.project.user.clients.dto.outbound.MediaUploadResponse;
import com.project.user.constants.Application;
import com.project.user.exceptions.MediaUploadException;
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

    public String saveUserProfilePicture(MultipartFile file, String userId) {
        ResponseEntity<MediaUploadResponse> response = mediaClient.saveMediaList(
            MediaUploadResource.builder()
                .files(List.of(file))
                .entityId(generateMediaId(Application.USER_MEDIA_PREFIX, userId))
                .filePath(userId)
                .build()
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getMediaReferencesList().get(0);
        }
        throw new MediaUploadException("Error saving media list");
    }

    public String updateUserProfilePicture(String reference, MultipartFile file, String userId) {
        if (reference != null)
            mediaClient.deleteMediaFile(reference);
        return saveUserProfilePicture(file, userId);
    }

    private String generateMediaId(@NonNull String prefix, @NonNull String entityId) {
        return prefix + entityId;
    }
}
