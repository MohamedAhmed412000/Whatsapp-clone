package com.project.core.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "ChatUserResponse",
    description = "Schema to hold the chat user response"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatUserResponse {
    @Schema(description = "The user Id", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;
    @Schema(description = "The user firstname", example = "Mohamed")
    private String firstname;
    @Schema(description = "The user lastname", example = "Ahmed")
    private String lastname;
    @Schema(description = "The user fullname", example = "Mohamed Ahmed")
    private String fullname;
    @Schema(description = "The user about section", example = "My Description")
    private String description;
    @Schema(description = "The user email", example = "mohamed.ahmed@example.com")
    private String email;
    @Schema(description = "The user profile image reference", example = "407dd10ad78743e49d0b58e7")
    private String imageFileReference;
    @Schema(description = "Flag is describing if the user is online", example = "true")
    private boolean isOnline;
    @Schema(description = "Flag is describing if the user role is admin or creator in the chat")
    private boolean isAdmin;
}
