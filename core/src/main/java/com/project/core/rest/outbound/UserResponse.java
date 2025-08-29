package com.project.core.rest.outbound;

import com.project.core.domain.dto.MobileNumber;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "The user Id", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;
    @Schema(description = "The user firstname", example = "Mohamed")
    private String firstName;
    @Schema(description = "The user lastname", example = "Ahmed")
    private String lastName;
    @Schema(description = "The user email", example = "mohamed.ahmed@example.com")
    private String email;
    private MobileNumber mobileNumber;
    @Schema(description = "The last time when the user was online")
    private LocalDateTime lastSeen;
    @Schema(description = "Flag to specify if the user is online")
    private boolean isOnline;
    @Schema(description = "The user profile image reference", example = "407dd10ad78743e49d0b58e7")
    private String profilePictureReference;
}
