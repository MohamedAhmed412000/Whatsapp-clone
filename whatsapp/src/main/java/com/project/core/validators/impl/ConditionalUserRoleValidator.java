package com.project.core.validators.impl;

import com.project.core.domain.enums.ChatUserOperationEnum;
import com.project.core.rest.inbound.ChatUserUpdateResource;
import com.project.core.validators.ValidResourceWhenUpdateChatUserRole;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ConditionalUserRoleValidator implements ConstraintValidator<ValidResourceWhenUpdateChatUserRole, ChatUserUpdateResource> {

    @Override
    public boolean isValid(ChatUserUpdateResource value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        if (value.getOperation().equals(ChatUserOperationEnum.MODIFY_EXISTING_USER_ROLE)
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
