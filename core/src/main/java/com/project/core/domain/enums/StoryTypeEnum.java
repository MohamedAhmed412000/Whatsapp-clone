package com.project.core.domain.enums;

public enum StoryTypeEnum {
    TEXT,
    MEDIA
    ;

    public static StoryTypeEnum getEnum(
        com.project.core.grpc.StoryTypeEnum storyTypeEnum) {
        return switch (storyTypeEnum) {
            case TEXT -> StoryTypeEnum.TEXT;
            case MEDIA -> StoryTypeEnum.MEDIA;
            default -> null;
        };
    }
}
