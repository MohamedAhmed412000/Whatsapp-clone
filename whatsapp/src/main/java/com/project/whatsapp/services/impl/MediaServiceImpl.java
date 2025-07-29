package com.project.whatsapp.services.impl;

import com.project.whatsapp.clients.MediaFeignClient;
import com.project.whatsapp.clients.dto.inbound.MediaUploadResource;
import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import com.project.whatsapp.clients.dto.outbound.MediaListResponse;
import com.project.whatsapp.constants.Application;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.domain.models.Message;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaServiceImpl {

    private final MediaFeignClient mediaFeignClient;

    public void saveMessageMedia(
        MediaUploadResource mediaUploadResource,
        Message message,
        String chatId
    ) {
        mediaFeignClient.saveMedia(
            MediaUploadResource.builder()
                .file(mediaUploadResource.getFile())
                .entityId(generateMediaMessageId(message.getId()))
                .filePath(chatId + File.separator + message.getId().toString())
                .build()
        );
    }

    public MediaListResponse retrieveMediaContentForMessage(Message message) {
        MediaListResponse mediaListResponse = new MediaListResponse();
        if (!message.getMessageType().equals(MessageTypeEnum.TEXT)) {
            ResponseEntity<MediaContentResponse> response = mediaFeignClient.getMediaContent(
                generateMediaMessageId(message.getId()));
            if (response.getStatusCode().is2xxSuccessful()) {
                assert response.getBody() != null;
                mediaListResponse.setMediaContentResponses(List.of(response.getBody()));
            }
        }
        return mediaListResponse;
    }

    private String generateMediaMessageId(@NonNull Long messageId) {
        return Application.MSG_MEDIA_PREFIX + messageId;
    }
}
