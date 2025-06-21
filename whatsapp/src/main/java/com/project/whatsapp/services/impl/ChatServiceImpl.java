package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import com.project.whatsapp.domain.models.Chat;
import com.project.whatsapp.domain.models.ChatUser;
import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.rest.outbound.ChatResponse;
import com.project.whatsapp.mappers.ChatMapper;
import com.project.whatsapp.repositories.ChatRepository;
import com.project.whatsapp.repositories.ChatUserRepository;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.services.ChatService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final UserRepository userRepository;
    private final ChatMapper chatMapper;

    @Transactional(readOnly = true)
    @Override
    public List<ChatResponse> getChatsByReceiverId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userId = securityContext.getAuthentication().getPrincipal().toString();
        UUID receiverId =  UUID.fromString(userId);
        return chatRepository.findChatsBySenderId(receiverId).stream()
            .map(chat -> chatMapper.toChatResponse(chat, receiverId))
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

        User sender = findUserById(senderId);
        List<User> receivers = receiversIds.stream().map(this::findUserById).toList();

        Chat chat = Chat.builder().name(chatName).isGroupChat(true).chatImageUrl(null).build();
        chatUserRepository.saveAll(
            Stream.concat(
                Stream.of(ChatUser.builder().chat(chat).user(sender).role(ChatUserRoleEnum.CREATOR).build()),
                receivers.stream().map(receiver -> ChatUser.builder().chat(chat).user(receiver)
                        .role(ChatUserRoleEnum.MEMBER).build())
            ).toList()
        );
        return chatRepository.save(chat).getId().toString();
    }

    private String createOneToOneChat(String senderId, String receiverId) {
        Optional<Chat> existingChat = chatRepository.findChatsBySenderIdAndReceiverId(
            UUID.fromString(senderId), UUID.fromString(receiverId));
        if (existingChat.isPresent()) {
            return existingChat.get().getId().toString();
        }

        User sender = findUserById(senderId);
        User receiver = findUserById(receiverId);

        Chat chat = Chat.builder().isGroupChat(false).chatImageUrl(null).build();
        ChatUser senderChatUser = ChatUser.builder().chat(chat)
            .user(sender).role(ChatUserRoleEnum.CREATOR).build();
        ChatUser receiverChatUser = ChatUser.builder().chat(chat)
            .user(receiver).role(ChatUserRoleEnum.MEMBER).build();
        chatUserRepository.saveAll(List.of(senderChatUser, receiverChatUser));
        return chatRepository.save(chat).getId().toString();
    }

    private User findUserById(String userId) {
        return userRepository.findByPublicId(UUID.fromString(userId)).orElseThrow(
            () -> new EntityNotFoundException(String.format("User with id=%s not found", userId))
        );
    }

}
