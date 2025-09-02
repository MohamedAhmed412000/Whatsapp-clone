package com.project.core.clients;

import com.project.core.clients.dto.inbound.UserStoriesListResource;
import com.project.core.clients.dto.outbound.UserStoriesListResponse;

public interface StoryClient {
    UserStoriesListResponse saveStoriesList(UserStoriesListResource resource);
}
