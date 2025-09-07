package com.project.user.services;

import com.project.user.rest.inbound.UserUpdateResource;
import com.project.user.rest.outbound.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers(String query);
    UserResponse getMyUserDetails();
    boolean updateUserDetails(UserUpdateResource resource);
}
