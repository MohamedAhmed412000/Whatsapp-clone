package com.project.whatsapp.validators;

import com.project.whatsapp.validators.impl.AtLeastOneFieldNotEmptyValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneFieldNotEmptyValidator.class)
@Documented
public @interface AtLeastOneFieldNotEmpty {
    String message() default "At least one of name, imageUrl, or description must be non-empty";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
