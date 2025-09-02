package com.project.story.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class MediaUploadException extends ApplicationException {

    public MediaUploadException() {
        super(StoryErrorsEnum.MEDIA_UPLOAD_ERROR);
    }

    public MediaUploadException(String message) {
        super(StoryErrorsEnum.MEDIA_UPLOAD_ERROR, message);
    }
}
