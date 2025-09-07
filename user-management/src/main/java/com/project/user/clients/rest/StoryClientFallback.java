package com.project.user.clients.rest;

import com.project.user.clients.dto.inbound.UserStoriesListResource;
import com.project.user.clients.dto.outbound.UserStoriesListResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "story.service.grpc.enabled", havingValue = "false")
public class StoryClientFallback implements StoryFeignClient {
    @Override
    public UserStoriesListResponse saveStoriesList(UserStoriesListResource resource) {
        return new UserStoriesListResponse(List.of());
    }
}
