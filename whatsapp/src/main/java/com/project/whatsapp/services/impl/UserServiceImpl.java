package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.models.User;
import com.project.whatsapp.mappers.UserMapper;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.rest.outbound.UserResponse;
import com.project.whatsapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

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
        return users.stream().map(userMapper::toUserResponse).toList();
    }

    public String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }

}