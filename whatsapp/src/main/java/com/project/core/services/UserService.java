package com.project.core.services;

import com.project.core.rest.inbound.UserUpdateResource;
import com.project.core.rest.outbound.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers(String query);
    UserResponse getMyUserDetails();
    boolean updateUserDetails(UserUpdateResource resource);
}
