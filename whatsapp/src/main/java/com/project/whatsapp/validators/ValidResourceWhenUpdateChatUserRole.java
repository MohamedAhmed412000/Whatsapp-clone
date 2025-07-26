package com.project.whatsapp.validators;

import com.project.whatsapp.validators.impl.ConditionalUserRoleValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalUserRoleValidator.class)
@Documented
public @interface ValidResourceWhenUpdateChatUserRole {
    String message() default "User role must be provided when operation is modify the user role";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
