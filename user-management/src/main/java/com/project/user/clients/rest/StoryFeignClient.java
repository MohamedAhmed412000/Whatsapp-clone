package com.project.user.clients.rest;

import com.project.user.clients.StoryClient;
import com.project.user.clients.dto.inbound.UserStoriesListResource;
import com.project.user.clients.dto.outbound.UserStoriesListResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "story-management", dismiss404 = true, fallback = StoryClientFallback.class)
@ConditionalOnProperty(name = "story.service.grpc.enabled", havingValue = "false")
public interface StoryFeignClient extends StoryClient {
    @GetMapping(value = "/api/v1/user-stories/me", produces = MediaType.APPLICATION_JSON_VALUE)
    UserStoriesListResponse saveStoriesList(UserStoriesListResource resource);
}
