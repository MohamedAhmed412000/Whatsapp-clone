package com.project.core.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MobileNumber {
    @Pattern(regexp = "^\\+?\\d{1,3}$")
    @Schema(description = "The phone country code", example = "+20")
    private String countryCode;
    @Size(min = 10, max = 11)
    @Schema(description = "The phone number", example = "01234567890")
    private String phoneNumber;
}

