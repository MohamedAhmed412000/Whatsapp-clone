package com.project.core.exceptions;

import com.project.commons.enums.IContentException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CoreErrorsEnum implements IContentException {
    UPDATE_ACTION_NOT_ALLOWED(HttpStatus.CONFLICT, "E100001", "Update action not allowed"),
    DELETE_ACTION_NOT_ALLOWED(HttpStatus.CONFLICT, "E100002", "Delete action not allowed"),
    USER_NOT_EXIST_IN_THE_CHAT(HttpStatus.NOT_FOUND, "E100003", "User doesn't exist in the chat"),
    INSUFFICIENT_ROLES_TO_TAKE_ACTION(HttpStatus.FORBIDDEN, "E100004", "Insufficient roles to take action"),
    USER_ALREADY_HAVE_ROLE(HttpStatus.CONFLICT, "E100005", "User already have the same role"),
    USER_ALREADY_EXISTS_IN_THE_CHAT(HttpStatus.CONFLICT, "E100006", "User already have the same role"),
    MEDIA_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E100007", "Media upload error"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "E100008", "User not found"),
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "E100009", "Chat not found"),
    ;

    private final HttpStatus httpStatus;
    private final String applicationCode;
    private final String message;

    @Override
    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
