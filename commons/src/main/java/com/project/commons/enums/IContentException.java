package com.project.commons.enums;

public interface IContentException {
    int getHttpStatusCode();
    String getApplicationCode();
    String getMessage();
}
