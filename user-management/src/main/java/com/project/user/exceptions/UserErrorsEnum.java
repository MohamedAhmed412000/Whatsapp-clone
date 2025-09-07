package com.project.user.exceptions;

import com.project.commons.enums.IContentException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserErrorsEnum implements IContentException {
    UPDATE_ACTION_NOT_ALLOWED(HttpStatus.CONFLICT, "E400001", "Update action not allowed"),
    DELETE_ACTION_NOT_ALLOWED(HttpStatus.CONFLICT, "E400002", "Delete action not allowed"),
    MEDIA_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E400003", "Media upload error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E400004", "User not found"),
    CONTACT_NOT_FOUND(HttpStatus.NOT_FOUND, "E400005", "Contact not found"),
    CONTACT_ALREADY_EXISTS(HttpStatus.CONFLICT, "E400006", "Contact already exists"),
    ;

    private final HttpStatus httpStatus;
    private final String applicationCode;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
