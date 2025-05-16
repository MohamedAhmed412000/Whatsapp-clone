package com.project.whatsapp.services.impl;

import com.project.whatsapp.repositories.ChatRepository;
import com.project.whatsapp.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;



}
