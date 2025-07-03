package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.dto.ChatWithUser;
import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import com.project.whatsapp.domain.models.Chat;
import com.project.whatsapp.domain.models.ChatUser;
import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.repositories.MessageRepository;
import com.project.whatsapp.rest.outbound.ChatResponse;
import com.project.whatsapp.mappers.ChatMapper;
import com.project.whatsapp.repositories.ChatRepository;
import com.project.whatsapp.repositories.ChatUserRepository;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.services.ChatService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final MongoTemplate mongoTemplate;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;
    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    @Override
    public List<ChatResponse> getChatsByReceiverId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userId = securityContext.getAuthentication().getPrincipal().toString();
        UUID receiverId = UUID.fromString(userId);
        return findChatsBySenderId(receiverId).stream()
            .map(chatWithUser -> {
                long unreadMessageCount = getUnreadMessageCount(chatWithUser.getChat().getId(),
                    receiverId);
                return chatMapper.toChatResponse(chatWithUser.getChat(), receiverId,
                    unreadMessageCount, chatWithUser.getLastSeen());
            })
            .toList();
    }

    @Transactional
    @Override
    public String createChat(String senderId, @NotEmpty List<String> receiversIds, boolean isGroupChat, String chatName) {
        if (!isGroupChat) {
            return createOneToOneChat(senderId, receiversIds.get(0));
        }
        if (receiversIds.isEmpty()) {
            throw new IllegalStateException("No receivers passed");
        }

        Chat chat = Chat.builder().name(chatName).isGroupChat(true).chatImageUrl(null).build();
        List<ChatUser> chatUserList = chatUserRepository.saveAll(
            Stream.concat(
                Stream.of(ChatUser.builder().chatId(chat.getId()).userId(UUID.fromString(senderId))
                    .role(ChatUserRoleEnum.CREATOR).build()),
                receiversIds.stream().map(receiverId -> ChatUser.builder().chatId(chat.getId())
                    .userId(UUID.fromString(receiverId)).role(ChatUserRoleEnum.MEMBER).build())
            ).toList()
        );

        chat.setMessageIds(List.of());
        chat.setUserIds(chatUserList.stream().map(ChatUser::getUserId).toList());
        return chatRepository.save(chat).getId().toString();
    }

    private String createOneToOneChat(String senderId, String receiverId) {
        Optional<Chat> existingChat = chatRepository.findChatsBySenderIdAndReceiverId(
            UUID.fromString(senderId), UUID.fromString(receiverId));
        if (existingChat.isPresent()) {
            return existingChat.get().getId().toString();
        }

        Optional<User> optionalSender = userRepository.findByPublicId(UUID.fromString(senderId));
        if (optionalSender.isEmpty()) {
            throw new MissingResourceException(String.format("User with id %s not found", senderId),
                User.class.getName(), senderId);
        }
        Optional<User> optionalReceiver = userRepository.findByPublicId(UUID.fromString(receiverId));
        if (optionalReceiver.isEmpty()) {
            throw new MissingResourceException(String.format("User with id %s not found", receiverId),
                User.class.getName(), receiverId);
        }

        Chat chat = Chat.builder()
            .name(senderId + '&' + optionalSender.get().getFullName() + '#' + receiverId + '&' +
                optionalReceiver.get().getFullName())
            .isGroupChat(false)
            .chatImageUrl(senderId + '&' + optionalSender.get().getProfilePictureUrl() + '#' +
                receiverId  + '&' + optionalReceiver.get().getProfilePictureUrl())
            .build();
        ChatUser senderChatUser = ChatUser.builder().chatId(chat.getId())
            .userId(UUID.fromString(senderId)).role(ChatUserRoleEnum.CREATOR).build();
        ChatUser receiverChatUser = ChatUser.builder().chatId(chat.getId())
            .userId(UUID.fromString(receiverId)).role(ChatUserRoleEnum.MEMBER).build();
        chatUserRepository.saveAll(List.of(senderChatUser, receiverChatUser));

        chat.setMessageIds(List.of());
        chat.setUserIds(List.of(UUID.fromString(senderId), UUID.fromString(receiverId)));
        return chatRepository.save(chat).getId().toString();
    }

    private List<ChatWithUser> findChatsBySenderId(UUID senderId) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("userId").is(senderId)),
            Aggregation.lookup("user", "userId", "_id", "userInfo"),
            Aggregation.unwind("userInfo"),
            Aggregation.lookup("chats", "chatId", "_id", "chatInfo"),
            Aggregation.unwind("chatInfo"),
            Aggregation.project()
                .and("chatInfo").as("chat")
                .and("userInfo.lastSeen").as("lastSeen")
        );

        AggregationResults<ChatWithUser> results = mongoTemplate.aggregate(
            aggregation, "chat_user", ChatWithUser.class
        );

        return results.getMappedResults();
    }

    private long getUnreadMessageCount(UUID chatId, UUID senderId) {
        Optional<ChatUser> optionalChatUser = chatUserRepository.findByChatIdAndUserId(chatId, senderId);
        if (optionalChatUser.isPresent()) {
            ChatUser chatUser = optionalChatUser.get();
            return messageRepository.findUnreadMessageCount(chatId, chatUser.getLastSeenMessageAt());
        }
        return 0;
    }
}
