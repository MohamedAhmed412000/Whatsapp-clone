package com.project.user.domain.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {

    private String newChatUserFullName;
    private Chat chat;

    @BeforeEach
    void setUp() {
        chat = Chat.builder()
            .id(UUID.randomUUID().toString())
            .name("c88438c5-260b-4e70-811a-0ceaead085e5&Mamdouh Mekky#fecead6f3e24-8998-d444-f28c-8e568f2c&Mohamed Ahmed")
            .build();
        newChatUserFullName = "Mohamed Ahmed Moftah";
    }

    @Test
    void updateChatName() {
        String userId = "fecead6f3e24-8998-d444-f28c-8e568f2c";
        String newChatName = chat.updateChatName(userId, newChatUserFullName);
        System.out.println(newChatName);
    }
}