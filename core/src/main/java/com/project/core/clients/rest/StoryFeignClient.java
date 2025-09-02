package com.project.core.clients.rest;

import com.project.core.clients.StoryClient;
import com.project.core.clients.dto.inbound.UserStoriesListResource;
import com.project.core.clients.dto.outbound.UserStoriesListResponse;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "story-management")
@ConditionalOnProperty(name = "story.service.grpc.enabled", havingValue = "false")
public interface StoryFeignClient extends StoryClient {
    @GetMapping(value = "/api/v1/user-stories/me", produces = MediaType.APPLICATION_JSON_VALUE)
    UserStoriesListResponse saveStoriesList(UserStoriesListResource resource);
}
