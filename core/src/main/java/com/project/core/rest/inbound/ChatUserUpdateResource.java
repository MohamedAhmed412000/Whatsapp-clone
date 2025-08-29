package com.project.core.rest.inbound;

import com.project.core.domain.enums.ChatUserOperationEnum;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.validators.ValidResourceWhenUpdateChatUserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
    name = "ChatUserUpdateResource",
    description = "Schema to hold the chat-user update data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidResourceWhenUpdateChatUserRole
public class ChatUserUpdateResource {
    @NotEmpty
    @Schema(description = "The user account Id", example = "550e8400-e29b-41d4-a716-446655440000")
    private String userId;
    @NotNull
    @Schema(description = "The chat user update operation", allowableValues = {
        "ADD_NEW_USER", "REMOVE_EXISTING_USER", "MODIFY_EXISTING_USER_ROLE"
    })
    private ChatUserOperationEnum operation;
    @Schema(description = "The user role in the chat [Mandatory in case operation=MODIFY_EXISTING_USER_ROLE]",
        allowableValues = { "CREATOR", "ADMIN", "MEMBER" })
    private ChatUserRoleEnum userRole;
}
