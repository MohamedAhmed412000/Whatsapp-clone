package com.project.whatsapp.validators.impl;

import com.project.whatsapp.domain.enums.ChatUserOperationEnum;
import com.project.whatsapp.rest.inbound.ChatUserUpdateResource;
import com.project.whatsapp.validators.ValidResourceWhenUpdateChatUserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConditionalUserRoleValidator implements ConstraintValidator<ValidResourceWhenUpdateChatUserRole, ChatUserUpdateResource> {

    @Override
    public boolean isValid(ChatUserUpdateResource value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.getOperation() == ChatUserOperationEnum.MODIFY_EXISTING_USER_ROLE
            && value.getUserRole() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("userRole is required when operation is MODIFY_EXISTING_USER_ROLE")
                .addPropertyNode("userRole")
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
