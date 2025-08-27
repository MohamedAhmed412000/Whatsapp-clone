package com.project.core.mappers;

import com.project.core.domain.dto.MobileNumber;
import com.project.core.domain.dto.UserWithRole;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.domain.models.User;
import com.project.core.rest.outbound.ChatUserResponse;
import com.project.core.rest.outbound.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user, boolean isMe) {
        UserResponse userResponse = UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .lastSeen(user.getLastSeen())
            .isOnline(isMe || user.isOnlineUser())
            .build();
        if (user.getProfilePictureReference() != null)
            userResponse.setProfilePictureReference(user.getProfilePictureReference());
        if (user.getCountryCode() != null && user.getPhoneNumber() != null)
            userResponse.setMobileNumber(
                MobileNumber.builder()
                    .countryCode(user.getCountryCode())
                    .phoneNumber(user.getPhoneNumber())
                    .build()
            );
        return userResponse;
    }

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
