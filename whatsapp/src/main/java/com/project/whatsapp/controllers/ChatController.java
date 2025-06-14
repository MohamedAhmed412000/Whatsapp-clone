package com.project.whatsapp.controllers;

import com.project.whatsapp.constants.Headers;
import com.project.whatsapp.rest.inbound.GroupChatResource;
import com.project.whatsapp.rest.outbound.ChatResponse;
import com.project.whatsapp.rest.outbound.StringResponse;
import com.project.whatsapp.services.ChatService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> createChat(
        @RequestParam(name = "sender-id") String senderId,
        @RequestParam(name = "receiver-id") String receiverId
    ) {
        final String chatId = chatService.createChat(senderId, List.of(receiverId), false, null);
        return ResponseEntity.ok(new StringResponse(chatId));
    }

    @PostMapping(value = "/group", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> createGroupChat(
        @Valid @RequestBody GroupChatResource resource
    ) {
        final String chatId = chatService.createChat(resource.getSenderId(), resource.getReceiversIds(),
            true, resource.getName());
        return ResponseEntity.ok(new StringResponse(chatId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatResponse>> getChatsByUser(
        @Parameter(hidden = true)
        @RequestHeader(Headers.USER_ID_HEADER) String userIdHeader
    ) {
        return ResponseEntity.ok(chatService.getChatsByReceiverId(userIdHeader));
    }

}
