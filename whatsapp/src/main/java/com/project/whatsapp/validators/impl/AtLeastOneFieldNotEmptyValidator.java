package com.project.whatsapp.validators.impl;

import com.project.whatsapp.rest.inbound.GroupChatUpdateResource;
import com.project.whatsapp.validators.AtLeastOneFieldNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOneFieldNotEmptyValidator implements ConstraintValidator<AtLeastOneFieldNotEmpty, GroupChatUpdateResource> {

    @Override
    public boolean isValid(GroupChatUpdateResource value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        return (value.getName() != null && !value.getName().isBlank()) ||
            (value.getImageUrl() != null && !value.getImageUrl().isBlank()) ||
            (value.getDescription() != null && !value.getDescription().isBlank());
    }
}
