package com.project.story.services.impl;

import com.project.story.domain.dto.StoryDetailsDto;
import com.project.story.domain.enums.StoryTypeEnum;
import com.project.story.domain.models.Story;
import com.project.story.exceptions.StoryNotFoundException;
import com.project.story.exceptions.UpdateActionNotAllowedException;
import com.project.story.mappers.StoryMapper;
import com.project.story.repositories.StoryRepository;
import com.project.story.rest.inbound.StoryCreationResource;
import com.project.story.rest.inbound.StoryUpdateResource;
import com.project.story.rest.outbound.UserStoriesListResponse;
import com.project.story.services.StoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoryServiceImpl implements StoryService {

    @Value("${media.hard-delete-flag}")
    private Boolean mediaHardDeleteFlag;
    private final StoryMapper storyMapper;
    private final StoryRepository storyRepository;
    private final MediaServiceImpl mediaService;

    @Override
    public Long createStory(StoryCreationResource resource) {
        StoryTypeEnum storyType = resource.getFile() != null? StoryTypeEnum.MEDIA: StoryTypeEnum.TEXT;
        Story story = Story.builder()
            .id(System.currentTimeMillis())
            .userId(getUserId())
            .content(resource.getContent())
            .storyType(storyType)
            .build();

        story.setCreatedAt(LocalDateTime.now());

        if (storyType.equals(StoryTypeEnum.MEDIA)) {
            String mediaReference = mediaService.saveUserStory(resource.getFile(), getUserId());
            story.setContent(null);
            story.setMediaReference(mediaReference);
        }

        return storyRepository.save(story).getId();
    }

    @Override
    public boolean updateStory(Long id, StoryUpdateResource storyUpdateResource) {
        try {
            Optional<Story> optionalStory = storyRepository.findById(id);
            if (optionalStory.isPresent()) {
                Story story = optionalStory.get();
                if (story.getStoryType().equals(StoryTypeEnum.MEDIA))
                    throw new UpdateActionNotAllowedException("Story Media Type is not allowed for modification");

                story.setContent(storyUpdateResource.getContent());
                storyRepository.save(story);
                return true;
            } else {
                throw new StoryNotFoundException("Story not found with id: " + id);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public boolean deleteStory(Long id) {
        try {
            if (mediaHardDeleteFlag) {
                storyRepository.deleteById(id);
            } else {
                Optional<Story> optionalStory = storyRepository.findById(id);
                if (optionalStory.isPresent()) {
                    Story story = optionalStory.get();
                    story.setDeleted(true);
                    storyRepository.save(story);
                } else {
                    throw new StoryNotFoundException("Story not found with id: " + id);
                }
            }
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return false;
    }

    @Override
    public UserStoriesListResponse getUserStories() {
        List<StoryDetailsDto> storyDetailsList = storyRepository.findStoryByUserId(getUserId())
            .stream().map(storyMapper::toStoryResponse).toList();
        return new UserStoriesListResponse(storyDetailsList);
    }

    private String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }
}
