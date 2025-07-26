package com.project.whatsapp.domain.dto;

import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import com.project.whatsapp.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithRole {
    private User user;
    private ChatUserRoleEnum role;
}
