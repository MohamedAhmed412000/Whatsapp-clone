package com.project.whatsapp.services.impl;

import com.project.whatsapp.clients.MediaFeignClient;
import com.project.whatsapp.clients.dto.inbound.MediaUploadResource;
import com.project.whatsapp.clients.dto.outbound.MediaContentResponse;
import com.project.whatsapp.clients.dto.outbound.MediaListResponse;
import com.project.whatsapp.constants.Application;
import com.project.whatsapp.domain.dto.Notification;
import com.project.whatsapp.domain.enums.MessageStateEnum;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.domain.enums.NotificationTypeEnum;
import com.project.whatsapp.domain.models.*;
import com.project.whatsapp.mappers.MessageMapper;
import com.project.whatsapp.repositories.ChatRepository;
import com.project.whatsapp.repositories.ChatUserRepository;
import com.project.whatsapp.repositories.MessageRepository;
import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;
import com.project.whatsapp.services.MessageService;
import com.project.whatsapp.services.NotificationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingResourceException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatUserServiceImpl chatUserServiceImpl;
    private final MediaFeignClient mediaFeignClient;
    private final MessageMapper messageMapper;
    private final NotificationService notificationService;

    @Value("${chats.page.max-messages-size:20}")
    private Integer chatMaxMessagesSize;

    @Override
    public void saveMessage(MessageResource request) {
        UUID senderId = UUID.fromString(request.getSenderId());
        Message message = Message.builder()
            .chatId(UUID.fromString(request.getChatId()))
            .content(request.getContent())
            .messageType(request.getMessageType())
            .senderId(senderId)
            .build();
        messageRepository.save(message);

        if (!request.getMessageType().equals(MessageTypeEnum.TEXT)) {
            request.getMediaResources().forEach(mediaResource ->
                mediaFeignClient.saveMedia(MediaUploadResource.builder()
                .file(mediaResource.getFile())
                .entityId(generateMediaMessageId(message.getId()))
                .filePath(request.getChatId() + File.separator + message.getId().toString())
                .build()));
        }

        Chat chat = chatRepository.findById(UUID.fromString(request.getChatId()))
            .orElseThrow(() -> new MissingResourceException(String.format("Chat with id=%s not found",
                request.getChatId()), Chat.class.getName(), request.getChatId()));
        chat.setLastMessage(message);
        chatRepository.save(chat);
        Notification notification = Notification.builder()
            .chatId(chat.getId())
            .chatName(chat.getChatName(senderId))
            .senderId(senderId)
            .messageType(message.getMessageType())
            .build();
        if (request.getMessageType().equals(MessageTypeEnum.TEXT)) {
            notification.setNotificationType(NotificationTypeEnum.MESSAGE);
            notification.setContent(message.getContent());
        } else {
            notification.setNotificationType(NotificationTypeEnum.MEDIA);
            notification.setMediaList(request.getMediaResources().stream().map(
                mediaResource -> {
                try {
                    return messageMapper.toMediaResponse(mediaResource.getFile());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).toList());
        }
        notificationService.sendNotification(
            chatUserRepository.getOtherChatUserIds(chat.getId(), senderId),
            notification
        );
    }

    @Override
    public List<MessageResponse> findChatMessages(String chatId, int page) {
        setLastViewTime(chatId);
        LocalDateTime lastSeenMessageAt = chatUserServiceImpl.findLastMessageViewedFromAllMembers(
            UUID.fromString(chatId));

        PageRequest pageRequest = PageRequest.of(page, chatMaxMessagesSize);
        return messageRepository.findMessagesByChatId(UUID.fromString(chatId), pageRequest).stream().map(message -> {
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
    public void setLastViewTime(String chatId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userId = securityContext.getAuthentication().getPrincipal().toString();
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(
            UUID.fromString(chatId), UUID.fromString(userId)
        ).orElseThrow(() -> new IllegalStateException("User is not member in this chat"));
        chatUser.setLastSeenMessageAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);

        Notification notification = Notification.builder()
            .chatId(UUID.fromString(chatId))
            .senderId(UUID.fromString(userId))
            .notificationType(NotificationTypeEnum.SEEN)
            .build();
        notificationService.sendNotification(
            chatUserRepository.getOtherChatUserIds(UUID.fromString(chatId), UUID.fromString(userId)),
            notification
        );
    }

    private String generateMediaMessageId(@NonNull Long messageId) {
        return Application.MSG_MEDIA_PREFIX + messageId;
    }

}
