package com.project.core.services.impl;

import com.project.core.domain.dto.UserWithRole;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.domain.enums.GroupChatModeEnum;
import com.project.core.domain.models.Chat;
import com.project.core.domain.models.ChatUser;
import com.project.core.exceptions.*;
import com.project.core.mappers.ChatUserMapper;
import com.project.core.repositories.ChatRepository;
import com.project.core.repositories.ChatUserRepository;
import com.project.core.rest.inbound.ChatUserUpdateResource;
import com.project.core.rest.outbound.ChatUserResponse;
import com.project.core.services.ChatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl implements ChatUserService {

    private final MongoTemplate mongoTemplate;
    private final ChatRepository chatRepository;
    private final ChatUserRepository chatUserRepository;
    private final ChatUserMapper userMapper;

    @Override
    public List<ChatUserResponse> getChatUsers(String chatId) {
        return findUsersByChatId(chatId).stream().map(userMapper::toChatUserResponse).toList();
    }

    @Override
    @Cacheable(value = "chatUsers", key = "#chatId")
    public List<String> getChatUserIds(String chatId) {
        return chatUserRepository.getChatAllUserIds(chatId).stream()
            .map(ChatUser::getUserId).toList();
    }

    @Transactional
    @Override
    public Boolean updateGroupChatUsers(String chatId, ChatUserUpdateResource resource) {
        Optional<Chat> chatOptional = chatRepository.findById(chatId);
        if (chatOptional.isEmpty() || !chatOptional.get().isGroupChat()) {
            throw new UpdateActionNotAllowedException("Chat with id " + chatId + " isn't allowed for this operation.");
        }
        Chat chat = chatOptional.get();
        Optional<ChatUser> meChatUserOptional = chatUserRepository.findByChatIdAndUserId(chatId, getUserId());
        if (meChatUserOptional.isPresent()) {
            ChatUser meChatUser = meChatUserOptional.get();
            switch (resource.getOperation()) {
                case ADD_NEW_USER:
                    return addNewChatUser(chat, meChatUser, resource.getUserId());
                case REMOVE_EXISTING_USER:
                    return removeExistingChatUser(chat, meChatUser, resource.getUserId());
                case MODIFY_EXISTING_USER_ROLE:
                    return modifyExistingChatUserRole(chat, meChatUser, resource.getUserId(),
                        resource.getUserRole());
                default:
                    break;
            }
        }
        throw new UserNotExistInChatException("The user doesn't exist in the chat");
    }

    private Boolean modifyExistingChatUserRole(Chat chat, ChatUser meChatUser,
                                               String userId, ChatUserRoleEnum role) {
        if (meChatUser.getRole().equals(ChatUserRoleEnum.MEMBER)) {
            throw new UpdateActionNotAllowedException("You are not allowed to modify group chat user role");
        }

        Optional<ChatUser> newChatUserOptional = chatUserRepository.findByChatIdAndUserId(chat.getId(), userId);
        if (newChatUserOptional.isPresent()) {
            ChatUser newChatUser = newChatUserOptional.get();
            if (newChatUser.getRole().equals(ChatUserRoleEnum.CREATOR) ||
                role.equals(ChatUserRoleEnum.CREATOR)) {
                throw new InsufficientRolesException("You aren't allowed to change to or from the creator role");
            }
            if (newChatUser.getRole().equals(role)) {
                throw new UserAlreadyHaveRoleException("User already have the same role");
            }
            newChatUser.setRole(role);
            chatUserRepository.save(newChatUser);
            return true;
        }
        return false;
    }

    private Boolean removeExistingChatUser(Chat chat, ChatUser meChatUser, String userId) {
        if ((chat.getGroupChatMode() == GroupChatModeEnum.USERS_MODIFICATION_RESTRICTED.getValue() ||
            chat.getGroupChatMode() == GroupChatModeEnum.ADMIN_RESTRICTED.getValue()) &&
            (meChatUser.getRole().equals(ChatUserRoleEnum.MEMBER))) {
            throw new DeleteActionNotAllowedException("You are not allowed to remove group chat user");
        }

        Optional<ChatUser> newChatUserOptional = chatUserRepository.findByChatIdAndUserId(chat.getId(), userId);
        if (newChatUserOptional.isPresent()) {
            if(newChatUserOptional.get().getRole().equals(ChatUserRoleEnum.CREATOR)) {
                throw new InsufficientRolesException("You are not allowed to remove group chat creator");
            }
            if (newChatUserOptional.get().getRole().equals(ChatUserRoleEnum.ADMIN) &&
                meChatUser.getRole().equals(ChatUserRoleEnum.MEMBER)) {
                throw new InsufficientRolesException("You are not allowed to remove group chat admin");
            }
            chat.setUserIds(chat.getUserIds().stream().filter(uId -> !uId.equals(userId)).toList());
            chatRepository.save(chat);
            chatUserRepository.delete(newChatUserOptional.get());
            return true;
        }
        return false;
    }

    private Boolean addNewChatUser(Chat chat, ChatUser meChatUser, String userId) {
        if ((chat.getGroupChatMode() == GroupChatModeEnum.USERS_MODIFICATION_RESTRICTED.getValue() ||
            chat.getGroupChatMode() == GroupChatModeEnum.ADMIN_RESTRICTED.getValue()) &&
            (meChatUser.getRole().equals(ChatUserRoleEnum.MEMBER))) {
            throw new InsufficientRolesException("You are not allowed to add group chat user");
        }
        if (chat.getUserIds().contains(userId)) {
            throw new UserAlreadyExistsInTheChatException("User already exists in this group chat");
        }

        try {
            ChatUser chatUser = ChatUser.builder().chatId(chat.getId()).userId(userId)
                .role(ChatUserRoleEnum.MEMBER).build();
            List<String> chatUserIds = chat.getUserIds();
            chatUserIds.add(userId);
            chat.setUserIds(chatUserIds);
            chatUserRepository.save(chatUser);
            chatRepository.save(chat);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    private List<UserWithRole> findUsersByChatId(String chatId) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("chat_id").is(chatId)),

            Aggregation.lookup("user", "user_id", "_id", "userInfo"),
            Aggregation.unwind("userInfo"),

            Aggregation.project()
                .and("userInfo").as("user")
                .and("role").as("role")
        );

        AggregationResults<UserWithRole> results = mongoTemplate.aggregate(
            aggregation, "chat_user", UserWithRole.class
        );

        return results.getMappedResults();
    }

    public String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }
}
