package com.project.whatsapp.services.impl;

import com.project.whatsapp.clients.dto.outbound.MediaListResponse;
import com.project.whatsapp.domain.dto.Notification;
import com.project.whatsapp.domain.dto.RepliedMessage;
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
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final MediaServiceImpl mediaService;
    private final MessageMapper messageMapper;
    private final NotificationService notificationService;
    private final MongoTemplate mongoTemplate;

    @Value("${chats.page.max-messages-size:20}")
    private Integer chatMaxMessagesSize;

    @Override
    public void saveMessage(MessageResource request) {
        String senderId = request.getSenderId();
        Message message = Message.builder()
            .chatId(request.getChatId())
            .content(request.getContent())
            .messageType(request.getMessageType())
            .isForwarded(request.isForwarded())
            .senderId(senderId)
            .build();

        if (request.getRepliedMessageId() != null) {
            Optional<Message> optionalRepliedMessage = messageRepository.findById(request.getRepliedMessageId());
            if (optionalRepliedMessage.isPresent()) {
                Message repliedMessageObject = optionalRepliedMessage.get();
                RepliedMessage repliedMessage = new RepliedMessage(repliedMessageObject);
                message.setRepliedMessage(repliedMessage);
            }
        }
        messageRepository.save(message);

        if (!request.getMessageType().equals(MessageTypeEnum.TEXT)) {
            request.getMediaResources().forEach(mediaResource ->
                mediaService.saveMessageMedia(mediaResource, message, request.getChatId()));
        }

        Chat chat = chatRepository.findById(request.getChatId())
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
    @Cacheable(value = "messages", key = "#chatId + '-' + #page")
    public List<MessageResponse> findChatMessages(String chatId, int page) {
        setLastViewTime(chatId);
        LocalDateTime lastSeenMessageAt = findLastMessageViewedFromAllMembers(chatId);

        PageRequest pageRequest = PageRequest.of(page, chatMaxMessagesSize);
        return messageRepository.findMessagesByChatId(chatId, pageRequest).stream().map(message -> {
            MediaListResponse mediaListResponse = mediaService.retrieveMediaContentForMessage(message);
            return messageMapper.toMessageResponse(message, (message.getCreatedAt().isAfter(lastSeenMessageAt))?
                MessageStateEnum.SENT: MessageStateEnum.SEEN, mediaListResponse.getMediaContentResponses());
        }).toList();
    }

    @Override
    public boolean editMessage(Long messageId, String messageContent) {
        String userId = getUserId();
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            if (!message.getSenderId().equals(userId) ||
                !message.getMessageType().equals(MessageTypeEnum.TEXT)
            ) {
                throw new RuntimeException("This action isn't allowed for this message");
            }
            message.setContent(messageContent);
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteMessage(Long messageId, boolean deleteForEveryone) {
        String userId = getUserId();
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            if (!message.getSenderId().equals(userId)) {
                throw new RuntimeException("This action isn't allowed for this message");
            }
            if (deleteForEveryone) {
                message.setDeletedForEveryone(true);
            } else {
                List<String> deleteUserIds = message.getDeleteForUserIds();
                deleteUserIds.add(userId);
                message.setDeleteForUserIds(deleteUserIds);
            }
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    private void setLastViewTime(String chatId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userId = securityContext.getAuthentication().getPrincipal().toString();
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(chatId, userId)
            .orElseThrow(() -> new IllegalStateException("User is not member in this chat"));
        chatUser.setLastSeenMessageAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);

        Notification notification = Notification.builder()
            .chatId(chatId)
            .senderId(userId)
            .notificationType(NotificationTypeEnum.SEEN)
            .build();
        notificationService.sendNotification(
            chatUserRepository.getOtherChatUserIds(chatId, userId),
            notification
        );
    }

    private String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }

    private LocalDateTime findLastMessageViewedFromAllMembers(String chatId) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("chatId").is(chatId)),
            Aggregation.sort(Sort.by("lastSeenMessageAt").ascending()),
            Aggregation.limit(1),
            Aggregation.project("lastSeenMessageAt")
        );

        AggregationResults<LocalDateTime> result = mongoTemplate.aggregate(
            aggregation,
            "chat_user",
            LocalDateTime.class
        );

        return result.getUniqueMappedResult();
    }
}
