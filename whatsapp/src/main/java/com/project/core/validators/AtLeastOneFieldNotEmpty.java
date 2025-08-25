package com.project.core.validators;

import com.project.core.validators.impl.AtLeastOneFieldNotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldNotEmptyValidator.class)
@Documented
public @interface AtLeastOneFieldNotEmpty {
    String message() default "At least one field must be non-empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
