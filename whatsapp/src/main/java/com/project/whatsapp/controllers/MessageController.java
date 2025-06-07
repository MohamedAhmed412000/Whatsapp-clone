package com.project.whatsapp.controllers;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;
import com.project.whatsapp.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@RequestBody MessageResource resource) {
        if (resource.getMessageType().equals(MessageTypeEnum.TEXT)) resource.setMediaResources(List.of());
        messageService.saveMessage(resource);
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void setMessagesToSeen(@RequestParam("chatId") String chatId, Authentication authentication) {
        messageService.setLastViewTime(chatId, authentication);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getChatMessages(
        @PathVariable("chatId") String chatId,
        @RequestParam("page") Integer page,
        Authentication authentication
    ) {
        return ResponseEntity.ok(messageService.findChatMessages(chatId, page, authentication));
    }

}
