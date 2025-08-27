package com.project.core.controllers;

import com.project.core.rest.inbound.MessageResource;
import com.project.core.rest.inbound.MessageUpdateResource;
import com.project.core.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.MessageResponse;
import com.project.core.services.MessageService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public void saveMessage(@Valid @ModelAttribute MessageResource resource) {
        messageService.saveMessage(resource);
    }

    @GetMapping(value = "/chat/{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<LocalDate, List<MessageResponse>>> getChatMessages(
        @NotEmpty @PathVariable("chatId") String chatId,
        @RequestParam("page") Integer page
    ) {
        return ResponseEntity.ok(messageService.findChatMessages(chatId, page));
    }

    @PatchMapping(value = "/{message-id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> editMessage(
        @NotEmpty @PathVariable("message-id") Long messageId,
        @Valid @RequestBody MessageUpdateResource resource
    ) {
        boolean isUpdated = messageService.editMessage(messageId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @DeleteMapping(value = "/{message-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> deleteMessage(
        @NotEmpty @PathVariable("message-id") Long messageId,
        @RequestParam(value = "delete-for-everyone", defaultValue = "false") boolean deleteForEveryone
    ) {
        boolean isUpdated = messageService.deleteMessage(messageId, deleteForEveryone);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

}
