package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.mappers.UserMapper;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.rest.inbound.UserUpdateResource;
import com.project.whatsapp.rest.outbound.UserResponse;
import com.project.whatsapp.services.UserService;
import com.project.whatsapp.utils.KeycloakUtil;
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
        return users.stream().map(user -> userMapper.toUserResponse(user, false)).toList();
    }

    @Override
    public UserResponse getMyUserDetails() {
        String userId = getUserId();
        Optional<User> userOptional = userRepository.findByPublicId(userId);
        if (userOptional.isEmpty()) throw new RuntimeException("User not found");
        return userMapper.toUserResponse(userOptional.get(), true);
    }

    @Override
    public boolean updateUserDetails(UserUpdateResource resource) {
        String userId = getUserId();
        Optional<User> userOptional = userRepository.findByPublicId(userId);
        if (userOptional.isEmpty()) throw new RuntimeException("User not found");
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