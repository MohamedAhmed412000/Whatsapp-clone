package com.project.core.controllers;

import com.project.core.rest.inbound.ChatUserUpdateResource;
import com.project.core.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.ChatUserResponse;
import com.project.core.services.ChatUserService;
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
public class ChatUserController {

    private final ChatUserService chatUserService;

    @GetMapping(value = "/{chat-id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatUserResponse>> getChatUsers(
        @NotEmpty @PathVariable("chat-id") String chatId
    ) {
        return ResponseEntity.ok(chatUserService.getChatUsers(chatId));
    }

    @PatchMapping(value = "/{chat-id}/update-users", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateGroupChatUsers(
        @NotEmpty @PathVariable("chat-id") String chatId,
        @Valid @RequestBody ChatUserUpdateResource resource
    ) {
        final Boolean isUpdated = chatUserService.updateGroupChatUsers(chatId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

}
