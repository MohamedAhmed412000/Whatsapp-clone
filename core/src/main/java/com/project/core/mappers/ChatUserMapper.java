package com.project.core.mappers;

import com.project.core.domain.dto.UserWithRole;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.rest.outbound.ChatUserResponse;
import org.springframework.stereotype.Component;

@Component
public class ChatUserMapper {

    public ChatUserResponse toChatUserResponse(UserWithRole userWithRole) {
        return ChatUserResponse.builder()
            .id(userWithRole.getUser().getId())
            .fullname(userWithRole.getUser().getFullName())
            .imageFileReference(userWithRole.getUser().getProfilePictureReference())
            .isAdmin(userWithRole.getRole().equals(ChatUserRoleEnum.CREATOR) ||
                userWithRole.getRole().equals(ChatUserRoleEnum.ADMIN))
            .build();
    }
}
