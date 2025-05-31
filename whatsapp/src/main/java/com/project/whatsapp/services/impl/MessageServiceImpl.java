package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.enums.MessageStateEnum;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

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
    private final MessageMapper messageMapper;

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

        // todo notification
    }

    public void saveMediaMessage() {

    }

    public List<MessageResponse> findChatMessages(String chatId, Authentication authentication) {
        setLastViewTime(chatId, authentication);
        LocalDateTime lastSeenMessageAt = chatUserRepository.findLastMessageViewedFromAllMembers(
            UUID.fromString(chatId));

        // todo notification
        return messageRepository.findMessagesByChatId(chatId).stream().map(message ->
            messageMapper.toMessageResponse(message, (message.getCreatedAt().isAfter(lastSeenMessageAt))?
                MessageStateEnum.SENT: MessageStateEnum.SEEN)).toList();
    }

    public void setLastViewTime(String chatId, Authentication authentication) {
        ChatUser chatUser = chatUserRepository.findByChatIdAndUserId(
            UUID.fromString(chatId), UUID.fromString(authentication.getName())
        ).orElseThrow(() -> new IllegalStateException("User is not member in this chat"));
        chatUser.setLastSeenMessageAt(LocalDateTime.now());
        chatUserRepository.save(chatUser);
    }

}
