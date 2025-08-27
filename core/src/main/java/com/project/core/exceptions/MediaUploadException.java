package com.project.core.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class MediaUploadException extends ApplicationException {

    public MediaUploadException() {
        super(CoreErrorsEnum.MEDIA_UPLOAD_ERROR);
    }

    public MediaUploadException(String message) {
        super(CoreErrorsEnum.MEDIA_UPLOAD_ERROR, message);
    }
}
