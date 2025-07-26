package com.project.whatsapp.controllers;

import com.project.whatsapp.rest.inbound.ChatUserUpdateResource;
import com.project.whatsapp.rest.inbound.GroupChatCreationResource;
import com.project.whatsapp.rest.inbound.GroupChatUpdateResource;
import com.project.whatsapp.rest.outbound.BooleanResponse;
import com.project.whatsapp.rest.outbound.ChatResponse;
import com.project.whatsapp.rest.outbound.ChatUserResponse;
import com.project.whatsapp.rest.outbound.StringResponse;
import com.project.whatsapp.services.ChatService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
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
        @RequestParam(name = "receiver-id") String receiverId
    ) {
        final String chatId = chatService.createOneToOneChat(receiverId);
        return ResponseEntity.ok(new StringResponse(chatId));
    }

    @PostMapping(value = "/group", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> createGroupChat(
        @Valid @RequestBody GroupChatCreationResource resource
    ) {
        final String chatId = chatService.createGroupChat(resource.getName(), resource.getImageUrl(),
            resource.getDescription(), resource.getReceiversIds());
        return ResponseEntity.ok(new StringResponse(chatId));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatResponse>> getChatsByUser() {
        return ResponseEntity.ok(chatService.getChatsByReceiverId());
    }

    @PatchMapping(value = "/{chat-id}/update", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateGroupChat(
        @NotEmpty @PathVariable("chat-id") String chatId,
        @Valid @RequestBody GroupChatUpdateResource resource
    ) {
        final Boolean isUpdated = chatService.updateGroupChat(chatId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @GetMapping(value = "/{chat-id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatUserResponse>> getChatUsers(
        @NotEmpty @PathVariable("chat-id") String chatId
    ) {
        return ResponseEntity.ok(chatService.getChatUsers(chatId));
    }

    @PatchMapping(value = "/{chat-id}/update-users", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateGroupChatUsers(
        @NotEmpty @PathVariable("chat-id") String chatId,
        @Valid @RequestBody ChatUserUpdateResource resource
    ) {
        final Boolean isUpdated = chatService.updateGroupChatUsers(chatId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @DeleteMapping("/{chat-id}")
    public ResponseEntity<BooleanResponse> deleteGroupChat(
        @NotEmpty @PathVariable("chat-id") String chatId
    ) {
        final Boolean isDeleted = chatService.deleteGroupChat(chatId);
        return ResponseEntity.ok(new BooleanResponse(isDeleted));
    }

}
