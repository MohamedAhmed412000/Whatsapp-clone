package com.project.core.controllers;

import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.core.rest.inbound.ChatUserUpdateResource;
import com.project.commons.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.ChatUserResponse;
import com.project.core.services.ChatService;
import com.project.core.services.ChatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Chat Users Controller",
    description = "CRUD Rest APIs for managing chat users"
)
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatUserController {

    private final ChatUserService chatUserService;
    private final ChatService chatService;

    @Operation(
        summary = "Retrieve all chat users",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get my chat users list",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatUserResponse.class)))
            )
        }
    )
    @GetMapping(value = "/{chat-id}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatUserResponse>> getChatUsers(
        @Schema(description = "The chat id", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @PathVariable("chat-id") String chatId
    ) {
        return ResponseEntity.ok(chatUserService.getChatUsers(chatId));
    }

    @Operation(
        summary = "Retrieve my single chats users",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get my single chats users",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))
            )
        }
    )
    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<String>> getChatUsers() {
        return ResponseEntity.ok(chatService.getSingleChatsUsers());
    }

    @Operation(
        summary = "Update an existing chat users",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Chat user updated successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found in the chat",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Unable to update the chat users",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{chat-id}/update-users", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateGroupChatUsers(
        @Schema(description = "The chat id", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @PathVariable("chat-id") String chatId,
        @Valid @RequestBody ChatUserUpdateResource resource
    ) {
        final Boolean isUpdated = chatUserService.updateGroupChatUsers(chatId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

}
