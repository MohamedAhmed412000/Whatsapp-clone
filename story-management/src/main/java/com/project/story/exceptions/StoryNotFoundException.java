package com.project.story.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class StoryNotFoundException extends ApplicationException {

    public StoryNotFoundException() {
        super(StoryErrorsEnum.STORY_NOT_FOUND);
    }

    public StoryNotFoundException(String message) {
        super(StoryErrorsEnum.STORY_NOT_FOUND, message);
    }
}
