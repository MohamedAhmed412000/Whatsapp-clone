package com.project.media.exceptions;

import com.project.commons.exceptions.ApplicationException;

public class FileDeletionException extends ApplicationException {

    public FileDeletionException() {
        super(MediaErrorsEnum.FILE_DELETION_ERROR);
    }

    public FileDeletionException(String message) {
        super(MediaErrorsEnum.FILE_DELETION_ERROR, message);
    }

    public FileDeletionException(String message, Throwable cause) {
        super(MediaErrorsEnum.FILE_DELETION_ERROR, message);
        this.initCause(cause);
    }

}
