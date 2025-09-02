package com.project.story.mappers;

import com.project.story.domain.dto.StoryDetailsDto;
import com.project.story.domain.enums.StoryTypeEnum;
import com.project.story.domain.models.Story;
import org.springframework.stereotype.Component;

@Component
public class StoryMapper {
    public StoryDetailsDto toStoryResponse(Story story) {
        return StoryDetailsDto.builder()
            .id(story.getId())
            .storyType(story.getStoryType())
            .content(story.getStoryType().equals(StoryTypeEnum.TEXT)? story.getContent(): null)
            .mediaReference(story.getStoryType().equals(StoryTypeEnum.TEXT)? null:
                story.getMediaReference())
            .createdAt(story.getCreatedAt())
            .build();
    }
}
