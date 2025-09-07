package com.project.user.domain.enums;

public enum StoryTypeEnum {
    TEXT,
    MEDIA
    ;

    public static StoryTypeEnum getEnum(
        com.project.user.grpc.StoryTypeEnum storyTypeEnum) {
        return switch (storyTypeEnum) {
            case TEXT -> StoryTypeEnum.TEXT;
            case MEDIA -> StoryTypeEnum.MEDIA;
            default -> null;
        };
    }
}
