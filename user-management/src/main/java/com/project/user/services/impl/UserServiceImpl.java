package com.project.user.services.impl;

import com.project.user.clients.StoryClient;
import com.project.user.clients.dto.inbound.UserStoriesListResource;
import com.project.user.clients.dto.outbound.UserStoriesListResponse;
import com.project.user.domain.models.User;
import com.project.user.exceptions.UserNotFoundException;
import com.project.user.mappers.UserMapper;
import com.project.user.repositories.UserRepository;
import com.project.user.rest.inbound.UserUpdateResource;
import com.project.user.rest.outbound.UserResponse;
import com.project.user.utils.KeycloakUtil;
import com.project.user.services.UserService;
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
            if (resource.getDescription() != null && !resource.getDescription().isEmpty())
                user.setDescription(resource.getDescription());
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

    @Override
    public boolean syncUserDetails() {
        String userId = getUserId();
        try {
            User user = keycloakUtil.getUserProfile(userId);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }
}