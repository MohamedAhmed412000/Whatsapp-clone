package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.rest.outbound.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
            .id(user.getId().toString())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .lastSeen(user.getLastSeen())
            .isOnline(user.isOnlineUser())
            .profilePictureUrl(user.getProfilePictureUrl())
            .build();
    }
}
