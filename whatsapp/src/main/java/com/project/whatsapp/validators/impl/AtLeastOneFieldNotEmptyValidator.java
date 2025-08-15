package com.project.whatsapp.validators.impl;

import com.project.whatsapp.validators.AtLeastOneFieldNotEmpty;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class AtLeastOneFieldNotEmptyValidator implements ConstraintValidator<AtLeastOneFieldNotEmpty, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        try {
            for (Field field : value.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object fieldValue = field.get(value);

                if (fieldValue != null) {
                    if (fieldValue instanceof String) {
                        if (!((String) fieldValue).isBlank()) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Error accessing fields for validation", e);
        }

        return false;
    }
}
