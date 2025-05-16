package com.project.whatsapp.domain.converters;

import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import org.springframework.core.convert.converter.Converter;

public class UserRolePriorityConverter implements Converter<ChatUserRoleEnum, Integer> {
    @Override
    public Integer convert(ChatUserRoleEnum role) {
        return role.getPriority();
    }
}
