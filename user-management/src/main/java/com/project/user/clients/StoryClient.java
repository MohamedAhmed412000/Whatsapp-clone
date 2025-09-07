package com.project.user.clients;

import com.project.user.clients.dto.inbound.UserStoriesListResource;
import com.project.user.clients.dto.outbound.UserStoriesListResponse;

public interface StoryClient {
    UserStoriesListResponse saveStoriesList(UserStoriesListResource resource);
}
