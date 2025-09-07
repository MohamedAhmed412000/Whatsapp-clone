package com.project.user.services;

import com.project.user.rest.inbound.UserContactCreationResource;
import com.project.user.rest.inbound.UserContactUpdateResource;
import com.project.user.rest.outbound.UserResponse;

import java.util.List;

public interface UserContactService {
    Long addNewContact(UserContactCreationResource resource);
    List<UserResponse> getUserContacts(String query);
    List<UserResponse> getUserBlockedContacts();
    boolean updateContact(String userId, UserContactUpdateResource resource);
    boolean pinContact(String userId);
    boolean unpinContact(String userId);
    boolean blockContact(String userId);
    boolean unblockContact(String userId);
    boolean isContactBlocked(String userId);
}
