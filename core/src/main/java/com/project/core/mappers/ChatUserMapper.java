package com.project.core.mappers;

import com.project.core.domain.dto.UserWithRole;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.rest.outbound.ChatUserResponse;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static com.project.core.constants.Application.LAST_ACTIVE_INTERVAL_IN_MINUTES;

@Component
public class ChatUserMapper {

    public ChatUserResponse toChatUserResponse(UserWithRole userWithRole) {
        return ChatUserResponse.builder()
            .id(userWithRole.getUser().getId())
            .firstname(userWithRole.getFirstName())
            .lastname(userWithRole.getLastName())
            .fullname(userWithRole.getUser().getFullName())
            .email(userWithRole.getUser().getEmail())
            .imageFileReference(userWithRole.getUser().getProfilePictureReference())
            .isOnline(isOnlineUser(userWithRole.getUser().getLastSeen()))
            .isAdmin(userWithRole.getRole().equals(ChatUserRoleEnum.CREATOR) ||
                userWithRole.getRole().equals(ChatUserRoleEnum.ADMIN))
            .build();
    }

    private boolean isOnlineUser(LocalDateTime lastSeen) {
        // last seen => 10:05
        // now (10:09) => active
        // now (10:12) => offline
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now(Clock.systemUTC())
            .minusMinutes(LAST_ACTIVE_INTERVAL_IN_MINUTES));
    }
}
