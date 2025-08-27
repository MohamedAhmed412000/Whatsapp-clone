package com.project.core.services.impl;

import com.project.core.domain.dto.MessageContent;
import com.project.core.domain.dto.Notification;
import com.project.core.domain.dto.RepliedMessage;
import com.project.core.domain.enums.MessageStateEnum;
import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.domain.enums.NotificationTypeEnum;
import com.project.core.domain.models.*;
import com.project.core.exceptions.ChatNotFoundException;
import com.project.core.exceptions.DeleteActionNotAllowedException;
import com.project.core.exceptions.UpdateActionNotAllowedException;
import com.project.core.mappers.MessageMapper;
import com.project.core.repositories.ChatRepository;
import com.project.core.repositories.ChatUserRepository;
import com.project.core.repositories.MessageRepository;
import com.project.core.rest.inbound.MessageResource;
import com.project.core.rest.inbound.MessageUpdateResource;
import com.project.core.rest.outbound.MessageResponse;
import com.project.core.services.MessageService;
import com.project.core.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConditionalOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

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
        String senderId = getUserId();
        Message message = Message.builder()
            .id(System.currentTimeMillis())
            .chatId(request.getChatId())
            .content(MessageContent.builder()
                .content(request.getContent())
                .build())
            .messageType(request.getMessageType())
            .isForwarded(Boolean.TRUE.equals(request.getIsForwarded()))
            .senderId(senderId)
            .build();
        message.setCreatedAt(LocalDateTime.now());

        if (request.getRepliedMessageId() != null) {
            Optional<Message> optionalRepliedMessage = messageRepository.findById(request.getRepliedMessageId());
            if (optionalRepliedMessage.isPresent()) {
                Message fullRepliedMessage = optionalRepliedMessage.get();
                RepliedMessage repliedMessage = new RepliedMessage(fullRepliedMessage);
                message.setRepliedMessage(repliedMessage);
            }
        }

        if (!request.getMessageType().equals(MessageTypeEnum.TEXT) &&
            !request.getMediaFiles().isEmpty()) {
            List<String> references = mediaService.saveMessageMediaList(
                request.getMediaFiles(),
                request.getChatId(),
                message.getId()
            );
            MessageContent messageContent = message.getContent();
            messageContent.setMediaReferences(references);
            message.setContent(messageContent);
        }
        messageRepository.save(message);

        Chat chat = chatRepository.findById(request.getChatId())
            .orElseThrow(() -> new ChatNotFoundException(String.format("Chat with id [%s] not found",
                request.getChatId())));
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
            notification.setContent(message.getContent().getContent());
        } else {
            notification.setNotificationType(NotificationTypeEnum.MEDIA);
            notification.setMediaReferencesList(message.getContent().getMediaReferences());
        }
        notificationService.sendNotification(
            chatUserRepository.getOtherChatUserIds(chat.getId(), senderId),
            notification
        );
    }

    @Override
    @Cacheable(value = "messages", key = "#chatId + '-' + #page")
    public Map<LocalDate, List<MessageResponse>> findChatMessages(String chatId, int page) {
        setLastViewTime(chatId);
        LocalDateTime lastSeenMessageAt = findLastMessageViewedFromAllMembers(chatId);

        Map<LocalDate, List<MessageResponse>> groupedMessagesMap = new LinkedHashMap<>(); // Keep order
        LocalDate currentDate = null;
        List<MessageResponse> messageResponses = null;

        PageRequest pageRequest = PageRequest.of(page, chatMaxMessagesSize);
        List<Message> messages = messageRepository.findMessagesByChatId(chatId, pageRequest);

        for (Message message : messages) {
            LocalDate messageDay = message.getCreatedAt().toLocalDate();

            if (currentDate == null || !currentDate.equals(messageDay)) {
                if (currentDate != null) {
                    groupedMessagesMap.put(currentDate, messageResponses);
                }
                messageResponses = new ArrayList<>();
                currentDate = messageDay;
            }

            messageResponses.add(
                messageMapper.toMessageResponse(
                    message,
                    message.getCreatedAt().isAfter(lastSeenMessageAt) ? MessageStateEnum.SENT : MessageStateEnum.SEEN
                )
            );
        }

        if (currentDate != null) {
            groupedMessagesMap.put(currentDate, messageResponses);
        }

        return groupedMessagesMap;
    }

    @Override
    public boolean editMessage(Long messageId, MessageUpdateResource resource) {
        String userId = getUserId();
        Optional<Message> messageOptional = messageRepository.findById(messageId);
        if (messageOptional.isPresent()) {
            Message message = messageOptional.get();
            if (!message.getSenderId().equals(userId) ||
                !message.getMessageType().equals(MessageTypeEnum.TEXT)
            ) {
                throw new UpdateActionNotAllowedException("This action isn't allowed for this message");
            }
            MessageContent messageContentObject = message.getContent();
            messageContentObject.setContent(resource.getContent());
            message.setContent(messageContentObject);
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
                throw new DeleteActionNotAllowedException("This action isn't allowed for this message");
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
        String userId = getUserId();
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
        Date minDate = Date.from(LocalDateTime.of(1970, 1, 1, 0, 0)
            .atZone(ZoneId.systemDefault()).toInstant());

        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("chat_id").is(chatId)),
            Aggregation.project()
                .and(
                    ConditionalOperators.ifNull("last_seen_message_at")
                        .thenValueOf(minDate.toString())
                ).as("lastSeenProcessed"),
            Aggregation.group()
                .min("lastSeenProcessed").as("oldestSeen")
        );

        AggregationResults<Document> result = mongoTemplate.aggregate(
            aggregation,
            "chat_user",
            Document.class
        );

        Document doc = result.getUniqueMappedResult();
        if (doc == null) {
            return LocalDateTime.MIN;
        }

        Date oldestSeen = doc.getDate("oldestSeen");
        return oldestSeen != null
            ? oldestSeen.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
            : LocalDateTime.MIN;
    }
}
