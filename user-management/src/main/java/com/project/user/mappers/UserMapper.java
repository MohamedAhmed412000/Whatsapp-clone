package com.project.user.mappers;

import com.project.user.clients.dto.outbound.StoryDetailsDto;
import com.project.user.domain.dto.MobileNumber;
import com.project.user.domain.dto.UserWithFullName;
import com.project.user.domain.models.User;
import com.project.user.rest.outbound.UserResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse toUserContactResponse(UserWithFullName userWithFullName, boolean isMe,
                                       List<StoryDetailsDto> storiesList) {
        UserResponse userResponse = UserResponse.builder()
            .id(userWithFullName.getUser().getId())
            .firstName(userWithFullName.getFirstname())
            .lastName(userWithFullName.getLastname())
            .fullName(userWithFullName.getUser().getFullName())
            .description(userWithFullName.getUser().getDescription())
            .email(userWithFullName.getUser().getEmail())
            .lastSeen(userWithFullName.getUser().getLastSeen())
            .isOnline(isMe || userWithFullName.getUser().isOnlineUser())
            .build();
        if (userWithFullName.getUser().getProfilePictureReference() != null)
            userResponse.setProfilePictureReference(userWithFullName.getUser().getProfilePictureReference());
        if (userWithFullName.getUser().getCountryCode() != null && userWithFullName.getUser().getPhoneNumber() != null)
            userResponse.setMobileNumber(
                MobileNumber.builder()
                    .countryCode(userWithFullName.getUser().getCountryCode())
                    .phoneNumber(userWithFullName.getUser().getPhoneNumber())
                    .build()
            );
        if (isMe) userResponse.setStories(storiesList);
        return userResponse;
    }

    public UserResponse toUserResponse(User user, boolean isMe,
                                              List<StoryDetailsDto> storiesList) {
        UserResponse userResponse = UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .fullName(user.getFullName())
            .description(user.getDescription())
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
        if (isMe) userResponse.setStories(storiesList);
        return userResponse;
    }
}
