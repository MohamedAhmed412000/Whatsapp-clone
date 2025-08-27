package com.project.media.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class MediaNotFoundException extends ApplicationException {

    public MediaNotFoundException() {
        super(MediaErrorsEnum.MEDIA_NOT_FOUND);
    }

    public MediaNotFoundException(String message) {
        super(MediaErrorsEnum.MEDIA_NOT_FOUND, message);
    }

    public MediaNotFoundException(String message, Throwable cause) {
        super(MediaErrorsEnum.MEDIA_NOT_FOUND, message);
        this.initCause(cause);
    }
}
