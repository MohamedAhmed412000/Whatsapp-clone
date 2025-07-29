package com.project.whatsapp.controllers;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import com.project.whatsapp.rest.inbound.MessageResource;
import com.project.whatsapp.rest.outbound.BooleanResponse;
import com.project.whatsapp.rest.outbound.MessageResponse;
import com.project.whatsapp.services.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@RequestBody MessageResource resource) {
        if (resource.getMessageType().equals(MessageTypeEnum.TEXT)) resource.setMediaResources(List.of());
        messageService.saveMessage(resource);
    }

    @GetMapping(value = "/chat/{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MessageResponse>> getChatMessages(
        @PathVariable("chatId") String chatId,
        @RequestParam("page") Integer page
    ) {
        return ResponseEntity.ok(messageService.findChatMessages(chatId, page));
    }

    @PatchMapping(value = "/{message-id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> editMessage(
        @PathVariable("message-id") Long messageId,
        @Valid @RequestBody String messageContent
    ) {
        boolean isUpdated = messageService.editMessage(messageId, messageContent);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @DeleteMapping(value = "/{message-id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> deleteMessage(
        @PathVariable("message-id") Long messageId,
        @RequestParam(value = "delete-for-everyone", defaultValue = "false") boolean deleteForEveryone
    ) {
        boolean isUpdated = messageService.deleteMessage(messageId, deleteForEveryone);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

}
