package com.project.story.services;

import com.project.story.rest.inbound.StoryCreationResource;
import com.project.story.rest.inbound.StoryUpdateResource;
import com.project.story.rest.outbound.ContactsStoriesListResponse;
import com.project.story.rest.outbound.UserStoriesListResponse;

public interface StoryService {
    Long createStory(StoryCreationResource storyCreationResource);
    boolean updateStory(Long id, StoryUpdateResource storyUpdateResource);
    boolean deleteStory(Long id);
    ContactsStoriesListResponse getUserContactsStories();
    UserStoriesListResponse getUserStories();
    void hideOutdatedStories();
}
