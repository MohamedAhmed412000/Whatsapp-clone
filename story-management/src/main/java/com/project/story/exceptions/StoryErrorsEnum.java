package com.project.story.exceptions;

import com.project.commons.enums.IContentException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum StoryErrorsEnum implements IContentException {
    MEDIA_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E300001", "Media upload error"),
    UPDATE_ACTION_NOT_ALLOWED(HttpStatus.CONFLICT, "E300002", "Update action not allowed"),
    STORY_NOT_FOUND(HttpStatus.NOT_FOUND, "E300003 ", "User not found"),
    ;

    private final HttpStatus httpStatus;
    private final String applicationCode;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
