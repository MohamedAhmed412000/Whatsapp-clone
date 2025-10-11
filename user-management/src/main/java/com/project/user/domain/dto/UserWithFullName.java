package com.project.user.domain.dto;

import com.project.user.domain.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserWithFullName {
    private User user;
    private String firstname;
    private String lastname;
}
