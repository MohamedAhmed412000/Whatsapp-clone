package com.project.whatsapp.domain.converters;

import com.project.whatsapp.domain.enums.ChatUserRoleEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class UserRolePriorityConverter implements AttributeConverter<ChatUserRoleEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ChatUserRoleEnum attribute) {
        return attribute.getPriority();
    }

    @Override
    public ChatUserRoleEnum convertToEntityAttribute(Integer dbData) {
        return ChatUserRoleEnum.getByPriority(dbData);
    }
}
