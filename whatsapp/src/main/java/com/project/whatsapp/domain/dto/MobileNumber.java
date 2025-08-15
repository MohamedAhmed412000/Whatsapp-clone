package com.project.whatsapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class MobileNumber {
    private String countryCode;
    private String phoneNumber;
}

