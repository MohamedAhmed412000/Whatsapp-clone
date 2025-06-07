package com.project.whatsapp.services.impl;

import com.project.whatsapp.clients.MediaFeignClient;
import com.project.whatsapp.clients.dto.inbound.MediaUploadResource;
import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import com.project.whatsapp.clients.dto.outbound.MediaListResponse;
import com.project.whatsapp.constants.Application;
import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.domain.models.Chat;
import com.project.whatsapp.domain.models.ChatUser;
import com.project.whatsapp.domain.models.Message;
import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.mappers.MessageMapper;
import com.project.whatsapp.repositories.ChatRepository;
import com.project.whatsapp.repositories.ChatUserRepository;
import com.project.whatsapp.repositories.MessageRepository;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;
import com.project.whatsapp.services.MessageService;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    private final MediaFeignClient mediaFeignClient;
    private final MessageMapper messageMapper;

    @Value("${chats.page.max-messages-size:20}")
    private Integer chatMaxMessagesSize;

    @Override
    public void saveMessage(MessageResource request) {
        Chat chat = chatRepository.findById(UUID.fromString(request.getChatId()))
            .orElseThrow(() -> new EntityNotFoundException(String.format("Chat with id=%s not found",
                request.getChatId())));
        User sender = userRepository.findByPublicId(UUID.fromString(request.getSenderId()))
            .orElseThrow(() -> new EntityNotFoundException(String.format("User with id=%s not found",
                request.getSenderId())));
        Message message = Message.builder()
            .chat(chat)
            .content(request.getContent())
            .messageType(request.getMessageType())
            .sender(sender)
            .build();
        messageRepository.save(message);

        if (!request.getMessageType().equals(MessageTypeEnum.TEXT)) {
            request.getMediaResources().forEach(mediaResource ->
                mediaFeignClient.saveMedia(MediaUploadResource.builder()
                .file(mediaResource.getFile())
                .entityId(generateMediaMessageId(message.getId()))
                .filePath(message.getChat().getId().toString() + File.separator + message.getId().toString())
                .build()));
        }

        // todo notification
    }

    @Override
    public List<MessageResponse> findChatMessages(String chatId, int page, Authentication authentication) {
        setLastViewTime(chatId, authentication);
        LocalDateTime lastSeenMessageAt = chatUserRepository.findLastMessageViewedFromAllMembers(
            UUID.fromString(chatId));

        // todo notification
        PageRequest pageRequest = PageRequest.of(page, chatMaxMessagesSize);
        return messageRepository.findMessagesByChatId(chatId, pageRequest).stream().map(message -> {
            MediaListResponse mediaListResponse = new MediaListResponse();
            if (!message.getMessageType().equals(MessageTypeEnum.TEXT)) {
                ResponseEntity<MediaContentResponse> response = mediaFeignClient.getMediaContent(
                    generateMediaMessageId(message.getId()));
                if (response.getStatusCode().is2xxSuccessful()) {
                    assert response.getBody() != null;
                    mediaListResponse.setMediaContentResponses(List.of(response.getBody()));
                }
            }
            return messageMapper.toMessageResponse(message, (message.getCreatedAt().isAfter(lastSeenMessageAt))?
                MessageStateEnum.SENT: MessageStateEnum.SEEN, mediaListResponse.getMediaContentResponses());
        }).toList();
    }

    @Override
    public void setLastViewTime(String chatId, Authentication authentication) {
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(
            UUID.fromString(chatId), UUID.fromString(authentication.getName())
        ).orElseThrow(() -> new IllegalStateException("User is not member in this chat"));
        chatUser.setLastSeenMessageAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);
    }

    private String generateMediaMessageId(@NonNull Long messageId) {
        return Application.MSG_MEDIA_PREFIX + messageId;
    }

}
