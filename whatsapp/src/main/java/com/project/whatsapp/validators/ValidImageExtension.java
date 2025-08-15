package com.project.whatsapp.validators;

import com.project.whatsapp.validators.impl.ImageExtensionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageExtensionValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImageExtension {
    String message() default "Only PNG, JPEG, JPG, or GIF images are allowed";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
