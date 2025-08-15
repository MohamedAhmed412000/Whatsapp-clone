package com.project.whatsapp.services;

import com.project.whatsapp.rest.inbound.UserUpdateResource;
import com.project.whatsapp.rest.outbound.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getUsers(String query);
    UserResponse getMyUserDetails();
    boolean updateUserDetails(UserUpdateResource resource);
}
