package com.project.core.validators;

import com.project.core.validators.impl.ConditionalMessageTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConditionalMessageTypeValidator.class)
@Documented
public @interface ValidResourceWhenCreateMessage {
    String message() default "Media must be provided or Test must be provided based on the message type";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
