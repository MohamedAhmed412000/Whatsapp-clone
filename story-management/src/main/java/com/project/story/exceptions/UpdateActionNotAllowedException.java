package com.project.story.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class UpdateActionNotAllowedException extends ApplicationException {

    public UpdateActionNotAllowedException() {
        super(StoryErrorsEnum.UPDATE_ACTION_NOT_ALLOWED);
    }

    public UpdateActionNotAllowedException(String message) {
        super(StoryErrorsEnum.UPDATE_ACTION_NOT_ALLOWED, message);
    }
}
