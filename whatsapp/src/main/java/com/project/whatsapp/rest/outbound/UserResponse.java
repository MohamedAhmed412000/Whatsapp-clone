package com.project.whatsapp.rest.outbound;

import com.project.whatsapp.domain.dto.MobileNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private MobileNumber mobileNumber;
    private LocalDateTime lastSeen;
    private boolean isOnline;
    private String profilePictureReference;
}
