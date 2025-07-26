package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.dto.UserWithRole;
import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.rest.outbound.ChatUserResponse;
import com.project.whatsapp.rest.outbound.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .lastSeen(user.getLastSeen())
            .isOnline(user.isOnlineUser())
            .profilePictureUrl(user.getProfilePictureUrl())
            .build();
    }

    public ChatUserResponse toChatUserResponse(UserWithRole userWithRole) {
        return ChatUserResponse.builder()
            .id(userWithRole.getUser().getId())
            .fullname(userWithRole.getUser().getFullName())
            .imageUrl(userWithRole.getUser().getProfilePictureUrl())
            .isAdmin(userWithRole.getRole().equals(ChatUserRoleEnum.CREATOR) ||
                userWithRole.getRole().equals(ChatUserRoleEnum.ADMIN))
            .build();
    }
}
