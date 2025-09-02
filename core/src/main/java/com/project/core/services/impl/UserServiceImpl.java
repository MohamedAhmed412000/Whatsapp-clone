package com.project.core.services.impl;

import com.project.core.clients.StoryClient;
import com.project.core.clients.dto.inbound.UserStoriesListResource;
import com.project.core.clients.dto.outbound.UserStoriesListResponse;
import com.project.core.domain.models.User;
import com.project.core.exceptions.UserNotFoundException;
import com.project.core.mappers.UserMapper;
import com.project.core.repositories.UserRepository;
import com.project.core.rest.inbound.UserUpdateResource;
import com.project.core.rest.outbound.UserResponse;
import com.project.core.services.UserService;
import com.project.core.utils.KeycloakUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final KeycloakUtil keycloakUtil;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MediaServiceImpl mediaServiceImpl;
    private final StoryClient storyClient;

    @Override
    @Cacheable(value = "users", key = "#root.target.getUserId() + '-' + #query")
    public List<UserResponse> getUsers(String query) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userId = securityContext.getAuthentication().getPrincipal().toString();
        List<User> users;
        if (query == null) query = "";
        query = query.trim();
        if (query.isEmpty()) {
            users = userRepository.findAllUsersExceptSelf(userId);
        }  else {
            users = userRepository.findUsersExceptSelf(userId, query);
        }
        return users.stream().map(user -> userMapper.toUserResponse(user, false, null)).toList();
    }

    @Override
    public UserResponse getMyUserDetails() {
        String userId = getUserId();
        Optional<User> userOptional = userRepository.findByPublicId(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException("User not found");
        UserStoriesListResponse storiesListResponse = storyClient.saveStoriesList(
            UserStoriesListResource.builder()
            .userId(userId)
            .build()
        );
        return userMapper.toUserResponse(userOptional.get(), true,
            storiesListResponse.getStoriesList());
    }

    @Override
    public boolean updateUserDetails(UserUpdateResource resource) {
        String userId = getUserId();
        Optional<User> userOptional = userRepository.findByPublicId(userId);
        if (userOptional.isEmpty()) throw new UserNotFoundException("User not found");
        User user = userOptional.get();

        try {
            if (resource.getFirstName() != null && !resource.getFirstName().isEmpty())
                user.setFirstName(resource.getFirstName());
            if (resource.getLastName() != null && !resource.getLastName().isEmpty())
                user.setLastName(resource.getLastName());
            if (resource.getMobileNumber() != null) {
                user.setCountryCode(resource.getMobileNumber().getCountryCode());
                user.setPhoneNumber(resource.getMobileNumber().getPhoneNumber());
            }
            if (resource.getProfilePicture() != null) {
                String userProfilePictureReference = mediaServiceImpl.updateUserProfilePicture(
                    user.getProfilePictureReference(), resource.getProfilePicture(), userId);
                user.setProfilePictureReference(userProfilePictureReference);
            }
            userRepository.save(user);

            keycloakUtil.updateUserProfile(userId, resource.getFirstName(), resource.getLastName(),
                user.getProfilePictureReference());
            return true;
        } catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            return false;
        }
    }

    public String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }
}