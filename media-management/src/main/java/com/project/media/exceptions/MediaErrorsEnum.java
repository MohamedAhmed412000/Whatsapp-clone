package com.project.media.exceptions;

import com.project.commons.enums.IContentException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum MediaErrorsEnum implements IContentException {
    MEDIA_NOT_FOUND(HttpStatus.NOT_FOUND, "E200001", "Media not found"),
    FILE_CREATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E200002", "File creation failed"),
    FILE_DELETION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E200003", "File deletion failed"),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "E200004", "File not found"),
    ;

    private final HttpStatus httpStatus;
    private final String applicationCode;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
