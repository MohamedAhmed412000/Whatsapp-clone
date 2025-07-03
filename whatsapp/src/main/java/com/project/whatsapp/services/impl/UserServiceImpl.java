package com.project.whatsapp.services.impl;

import com.project.whatsapp.mappers.UserMapper;
import com.project.whatsapp.repositories.UserRepository;
import com.project.whatsapp.rest.outbound.UserResponse;
import com.project.whatsapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserResponse> getUsers() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        String userId = securityContext.getAuthentication().getPrincipal().toString();
        return userRepository.findAllUsersExceptSelf(UUID.fromString(userId))
            .stream().map(userMapper::toUserResponse).toList();
    }

}
