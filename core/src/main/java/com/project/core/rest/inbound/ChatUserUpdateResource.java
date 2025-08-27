package com.project.core.rest.inbound;

import com.project.core.domain.enums.ChatUserOperationEnum;
import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.validators.ValidResourceWhenUpdateChatUserRole;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ValidResourceWhenUpdateChatUserRole
public class ChatUserUpdateResource {
    @NotEmpty
    private String userId;
    @NotNull
    private ChatUserOperationEnum operation;
    private ChatUserRoleEnum userRole;
}
