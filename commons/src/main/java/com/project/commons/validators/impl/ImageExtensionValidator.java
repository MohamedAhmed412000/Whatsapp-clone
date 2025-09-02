package com.project.commons.validators.impl;

import com.project.commons.validators.ValidImageExtension;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class ImageExtensionValidator implements ConstraintValidator<ValidImageExtension, MultipartFile> {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("png", "jpeg", "jpg", "gif");

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        String fileName = value.getOriginalFilename();
        if (fileName == null || !fileName.contains(".")) {
            return false;
        }

        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}