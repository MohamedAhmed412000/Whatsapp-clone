package com.project.core.services.impl;

import com.project.core.domain.dto.ChatWithUser;
import com.project.core.domain.dto.MessageContent;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.domain.enums.GroupChatModeEnum;
import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.domain.enums.SystemMessageEnum;
import com.project.core.domain.models.Chat;
import com.project.core.domain.models.ChatUser;
import com.project.core.domain.models.Message;
import com.project.core.domain.models.User;
import com.project.core.exceptions.ChatNotFoundException;
import com.project.core.exceptions.DeleteActionNotAllowedException;
import com.project.core.exceptions.UpdateActionNotAllowedException;
import com.project.core.exceptions.UserNotFoundException;
import com.project.core.repositories.MessageRepository;
import com.project.core.repositories.UserRepository;
import com.project.core.rest.inbound.GroupChatUpdateResource;
import com.project.core.rest.outbound.ChatResponse;
import com.project.core.mappers.ChatMapper;
import com.project.core.repositories.ChatRepository;
import com.project.core.repositories.ChatUserRepository;
import com.project.core.services.ChatService;
import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
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
    private final MediaServiceImpl mediaServiceImpl;

    @Override
//    @Cacheable(value = "chatDetails", key = "#chatId")
    public ChatResponse getChatDetails(String chatId) {
        String myUserId = getUserId();
        return findChatsBySenderId(myUserId, chatId).stream()
            .map(chatWithUser -> {
                long unreadMessageCount = getUnreadMessageCount(chatWithUser.getChat().getId(), myUserId);
                return chatMapper.toChatResponse(chatWithUser.getChat(), myUserId,
                    unreadMessageCount, chatWithUser.getLastSeen());
            })
            .toList().get(0);
    }

    @Override
    @Cacheable(value = "chats", key = "#root.target.getUserId()")
    public List<ChatResponse> getChatsByReceiverId() {
        String myUserId = getUserId();
        return findChatsBySenderId(myUserId, null).stream()
            .map(chatWithUser -> {
                long unreadMessageCount = getUnreadMessageCount(chatWithUser.getChat().getId(), myUserId);
                return chatMapper.toChatResponse(chatWithUser.getChat(), myUserId,
                    unreadMessageCount, chatWithUser.getLastSeen());
            })
            .toList();
    }

    @Transactional
    @Override
    public String createGroupChat(String chatName, String description, MultipartFile groupChatProfileFile,
                                   List<String> receiversIds) {
        String senderId = getUserId();
        Chat chat = Chat.builder().id(UUID.randomUUID().toString()).name(chatName).isGroupChat(true)
            .isNew(true).groupChatMode(GroupChatModeEnum.NORMAL.getValue()).chatImageReference(null).build();
        Message message = Message.builder().id(System.currentTimeMillis()).chatId(chat.getId())
            .messageType(MessageTypeEnum.SYSTEM).senderId("SYSTEM")
            .content(MessageContent.builder().content(SystemMessageEnum.CHAT_CREATED.getContent()).build())
            .build();
        message.setCreatedAt(LocalDateTime.now());
        chat.setLastMessage(message);
        if (description != null) chat.setDescription(description);
        if (groupChatProfileFile != null) {
            String groupChatProfilePictureReference = mediaServiceImpl.saveGroupChatProfilePicture(
                groupChatProfileFile, chat.getId());
            chat.setChatImageReference(groupChatProfilePictureReference);
        }
        List<ChatUser> chatUserList = chatUserRepository.saveAll(
            Stream.concat(
                Stream.of(ChatUser.builder().chatId(chat.getId()).userId(senderId)
                    .role(ChatUserRoleEnum.CREATOR).build()),
                receiversIds.stream().map(receiverId -> ChatUser.builder().chatId(chat.getId())
                    .userId(receiverId).role(ChatUserRoleEnum.MEMBER).build())
            ).toList()
        );
        messageRepository.save(message);

        chat.setUserIds(chatUserList.stream().map(ChatUser::getUserId).toList());
        return chatRepository.save(chat).getId();
    }

    @Transactional
    @Override
    public String createOneToOneChat(String receiverId) {
        String senderId = getUserId();
        Optional<Chat> existingChat = chatRepository.findChatsBySenderIdAndReceiverId(senderId, receiverId);
        if (existingChat.isPresent()) {
            return existingChat.get().getId();
        }

        Optional<User> optionalSender = userRepository.findByPublicId(senderId);
        if (optionalSender.isEmpty()) {
            throw new UserNotFoundException(String.format("User with id [%s] not found", senderId));
        }
        Optional<User> optionalReceiver = userRepository.findByPublicId(receiverId);
        if (optionalReceiver.isEmpty()) {
            throw new UserNotFoundException(String.format("User with id [%s] not found", receiverId));
        }

        Chat chat = Chat.builder()
            .id(UUID.randomUUID().toString())
            .name(senderId + '&' + optionalSender.get().getFullName() + '#' + receiverId + '&' +
                optionalReceiver.get().getFullName()).isGroupChat(false)
            .isNew(true)
            .chatImageReference(senderId + '&' + optionalSender.get().getProfilePictureReference() + '#' +
                receiverId + '&' + optionalReceiver.get().getProfilePictureReference()).build();
        Message message = Message.builder().id(System.currentTimeMillis()).chatId(chat.getId())
            .messageType(MessageTypeEnum.SYSTEM).senderId("SYSTEM")
            .content(MessageContent.builder().content(SystemMessageEnum.CHAT_CREATED.getContent()).build())
            .build();
        message.setCreatedAt(LocalDateTime.now());
        chat.setLastMessage(message);
        ChatUser senderChatUser = ChatUser.builder().chatId(chat.getId())
            .userId(senderId).role(ChatUserRoleEnum.CREATOR).build();
        ChatUser receiverChatUser = ChatUser.builder().chatId(chat.getId())
            .userId(receiverId).role(ChatUserRoleEnum.MEMBER).build();
        chatUserRepository.saveAll(List.of(senderChatUser, receiverChatUser));
        messageRepository.save(message);

        chat.setUserIds(List.of(senderId, receiverId));
        return chatRepository.save(chat).getId();
    }
    
    @Transactional
    @Override
    public Boolean updateGroupChat(String chatId, GroupChatUpdateResource resource) {
        Optional<Chat> optionalChat = chatRepository.findById(chatId);
        if (optionalChat.isPresent()) {
            Chat chat = optionalChat.get();
            if (!chat.isGroupChat()) {
                throw new UpdateActionNotAllowedException("Action not allowed for 1-to-1 chat");
            }

            try {
                if (resource.getName() != null) chat.setName(resource.getName());
                if (resource.getDescription() != null) chat.setDescription(resource.getDescription());
                if (resource.getChatImage() != null) {
                    String groupChatProfilePictureReference = mediaServiceImpl.updateGroupChatProfilePicture(
                        chat.getChatImageReference(), resource.getChatImage(), chatId);
                    chat.setChatImageReference(groupChatProfilePictureReference);
                }
                chatRepository.save(chat);
                return true;
            } catch (Exception exception) {
                return false;
            }
        }
        throw new ChatNotFoundException("Chat with id [" + chatId + "] not found");
    }

    @Transactional
    @Override
    public Boolean deleteGroupChat(String chatId) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isEmpty() || !chatOptional.get().isGroupChat()) {
            throw new DeleteActionNotAllowedException("Chat with id " + chatId + " isn't allowed for this operation.");
        }
        try {
            chatRepository.delete(chatOptional.get());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }

    private List<ChatWithUser> findChatsBySenderId(String senderId, String chatId) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("user_id").is(senderId)),
            Aggregation.match(chatId != null ? Criteria.where("chat_id").is(chatId) :
                Criteria.where("_id").exists(true)),

            Aggregation.lookup("chat", "chat_id", "_id", "chatInfo"),
            Aggregation.unwind("chatInfo"),
            Aggregation.sort(Sort.by(Sort.Direction.DESC, "chatInfo.last_message.created_at")),

            Aggregation.addFields()
                .addField("otherUserId")
                .withValue(
                    ConditionalOperators.when(ComparisonOperators.Eq.valueOf("chatInfo.is_group_chat").equalToValue(false))
                        .thenValueOf(
                            ArrayOperators.ArrayElemAt.arrayOf(
                                ArrayOperators.Filter
                                    .filter("chatInfo.user_ids")
                                    .as("id")
                                    .by(
                                        ComparisonOperators.Ne.valueOf("$$id").notEqualToValue(senderId)
                                    )
                            ).elementAt(0)
                        )
                        .otherwise(ConvertOperators.ToDate.toDate(
                            LiteralOperators.Literal.asLiteral("2000-01-04T12:00:00Z")))
                ).build(),

            Aggregation.lookup("user", "otherUserId", "_id", "otherUserInfo"),
            Aggregation.unwind("otherUserInfo", true),

            Aggregation.project()
                .and("chatInfo").as("chat")
                .and(
                    ConditionalOperators
                        .when(ComparisonOperators.Eq.valueOf("chatInfo.is_group_chat").equalToValue(false))
                        .thenValueOf(
                            DateOperators.dateOf("otherUserInfo.last_seen")
                                .toString("%Y-%m-%dT%H:%M:%S.%LZ")
                                .withTimezone(DateOperators.Timezone.valueOf("+00:00"))
                        )
                        .otherwise("2000-01-04T12:00:00Z")
                ).as("lastSeen")
        );

        AggregationResults<ChatWithUser> results = mongoTemplate.aggregate(
            aggregation, "chat_user", ChatWithUser.class
        );
        return results.getMappedResults();
    }

    private long getUnreadMessageCount(String chatId, String senderId) {
        Optional<ChatUser> optionalChatUser = chatUserRepository.findByChatIdAndUserId(chatId, senderId);
        if (optionalChatUser.isPresent()) {
            ChatUser chatUser = optionalChatUser.get();
            return messageRepository.findUnreadMessageCount(chatId, chatUser.getLastSeenMessageAt());
        }
        return 0;
    }
}
