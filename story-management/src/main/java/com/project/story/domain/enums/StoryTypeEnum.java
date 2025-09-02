package com.project.story.domain.enums;

public enum StoryTypeEnum {
    TEXT,
    MEDIA
    ;

    public static com.project.story.grpc.StoryTypeEnum getProtoEnum(
        StoryTypeEnum storyTypeEnum) {
        return switch (storyTypeEnum) {
            case TEXT -> com.project.story.grpc.StoryTypeEnum.TEXT;
            case MEDIA -> com.project.story.grpc.StoryTypeEnum.MEDIA;
        };
    }
}
