package com.project.user.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class MediaUploadException extends ApplicationException {

    public MediaUploadException() {
        super(UserErrorsEnum.MEDIA_UPLOAD_ERROR);
    }

    public MediaUploadException(String message) {
        super(UserErrorsEnum.MEDIA_UPLOAD_ERROR, message);
    }
}
