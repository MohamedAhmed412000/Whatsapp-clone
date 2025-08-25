package com.project.core.domain.dto;

import com.project.core.domain.enums.ChatUserRoleEnum;
import com.project.core.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithRole {
    private User user;
    private ChatUserRoleEnum role;
}
