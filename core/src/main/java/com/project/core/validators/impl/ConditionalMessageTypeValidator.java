package com.project.core.validators.impl;

import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.rest.inbound.MessageResource;
import com.project.core.validators.ValidResourceWhenCreateMessage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConditionalMessageTypeValidator implements ConstraintValidator<ValidResourceWhenCreateMessage, MessageResource> {

    @Override
    public boolean isValid(MessageResource value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.getMessageType().equals(MessageTypeEnum.TEXT) &&
            (value.getContent() == null || value.getContent().isEmpty())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("content is required when messageType is TEXT")
                .addPropertyNode("content")
                .addConstraintViolation();
            return false;
        } else if (value.getMessageType().equals(MessageTypeEnum.AUDIO) &&
            (value.getMediaFiles() == null || value.getMediaFiles().size() != 1)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("mediaFiles requiring 1 audio file when messageType is AUDIO")
                .addPropertyNode("mediaFiles")
                 .addConstraintViolation();
            return false;
        } else if (value.getMessageType().equals(MessageTypeEnum.MEDIA) &&
            (value.getMediaFiles() == null || value.getMediaFiles().isEmpty())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("mediaFiles are required when messageType is MEDIA")
                    .addPropertyNode("mediaFiles")
                    .addConstraintViolation();
                return false;
        }
        return true;
    }
}
