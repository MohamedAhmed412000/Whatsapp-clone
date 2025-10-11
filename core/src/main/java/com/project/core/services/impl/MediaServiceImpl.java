package com.project.core.services.impl;

import com.project.core.clients.MediaClient;
import com.project.core.clients.dto.inbound.MediaUploadResource;
import com.project.core.clients.dto.outbound.MediaUploadResponse;
import com.project.core.constants.Application;
import com.project.core.exceptions.MediaUploadException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl {

    private final MediaClient mediaClient;

    public List<String> saveMessageMediaList(
        List<MultipartFile> files, String chatId, Long messageId
    ) {
        ResponseEntity<MediaUploadResponse> response = mediaClient.saveMediaList(
            MediaUploadResource.builder()
                .files(files)
                .entityId(generateMediaId(Application.MSG_MEDIA_PREFIX, messageId.toString()))
                .filePath(chatId + "," + messageId)
                .build()
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getMediaReferencesList();
        }
        throw new MediaUploadException("Error saving media list");
    }

    public String saveGroupChatProfilePicture(MultipartFile file, String chatId) {
        ResponseEntity<MediaUploadResponse> response = mediaClient.saveMediaList(
            MediaUploadResource.builder()
                .files(List.of(file))
                .entityId(generateMediaId(Application.CHAT_MEDIA_PREFIX, chatId))
                .filePath(chatId +  "," + "PROFILE")
                .build()
        );

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody().getMediaReferencesList().get(0);
        }
        throw new MediaUploadException("Error saving media list");
    }

    public String updateGroupChatProfilePicture(String reference, MultipartFile file, String chatId) {
        if (reference != null) mediaClient.deleteMediaFile(reference);
        return saveGroupChatProfilePicture(file, chatId);
    }

    private String generateMediaId(@NonNull String prefix, @NonNull String entityId) {
        return prefix + entityId;
    }
}
