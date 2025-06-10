package com.project.whatsapp.controllers;

import com.project.whatsapp.constants.Headers;
import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.MessageResponse;
import com.project.whatsapp.services.MessageService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public void setMessagesToSeen(
        @RequestParam("chatId") String chatId,
        @Parameter(hidden = true)
        @RequestHeader(Headers.USER_ID_HEADER) String userIdHeader
    ) {
        messageService.setLastViewTime(chatId, userIdHeader);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<MessageResponse>> getChatMessages(
        @PathVariable("chatId") String chatId,
        @RequestParam("page") Integer page,
        @Parameter(hidden = true)
        @RequestHeader(Headers.USER_ID_HEADER) String userIdHeader
    ) {
        return ResponseEntity.ok(messageService.findChatMessages(chatId, page, userIdHeader));
    }

}
