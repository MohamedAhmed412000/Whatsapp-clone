package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.dto.MobileNumber;
import com.project.whatsapp.domain.dto.UserWithRole;
import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.rest.outbound.ChatUserResponse;
import com.project.whatsapp.rest.outbound.UserResponse;
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
