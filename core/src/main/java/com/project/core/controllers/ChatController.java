package com.project.core.controllers;

import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.commons.rest.outbound.BooleanResponse;
import com.project.commons.rest.outbound.StringResponse;
import com.project.core.rest.inbound.GroupChatCreationResource;
import com.project.core.rest.inbound.GroupChatUpdateResource;
import com.project.core.rest.outbound.ChatResponse;
import com.project.core.services.ChatService;
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
    name = "Chats Controller",
    description = "CRUD Rest APIs for managing chats"
)
@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @Operation(
        summary = "Create a new 1-to-1 chat",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Chat created successfully",
                content = @Content(schema = @Schema(implementation = StringResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> createChat(
        @Schema(description = "The user id to create the chat with", example = "550e8400-e29b-41d4-a716-446655440000")
        @RequestParam(name = "receiver-id") String receiverId
    ) {
        final String chatId = chatService.createOneToOneChat(receiverId);
        return ResponseEntity.ok(new StringResponse(chatId));
    }

    @Operation(
        summary = "Create a new group chat",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Chat created successfully",
                content = @Content(schema = @Schema(implementation = StringResponse.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Media upload error",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PostMapping(value = "/group", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> createGroupChat(
        @Valid @ModelAttribute GroupChatCreationResource resource
    ) {
        final String chatId = chatService.createGroupChat(resource.getName(), resource.getDescription(),
            resource.getFile(), resource.getReceiversIds());
        return ResponseEntity.ok(new StringResponse(chatId));
    }

    @Operation(
        summary = "Retrieve my chats",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get my chats list",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = ChatResponse.class)))
            )
        }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChatResponse>> getChatsByUser() {
        return ResponseEntity.ok(chatService.getChatsByReceiverId());
    }

    @Operation(
        summary = "Retrieve chat details by chatId",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get chat details by chatId",
                content = @Content(schema = @Schema(implementation = ChatResponse.class))
            )
        }
    )
    @GetMapping(value = "/{chat-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ChatResponse> getChatDetails(
        @PathVariable("chat-id") String chatId
    ) {
        return ResponseEntity.ok(chatService.getChatDetails(chatId));
    }

    @Operation(
        summary = "Update an existing chat",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Chat updated successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Chat not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Unable to update the chat",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{chat-id}/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateGroupChat(
        @Schema(description = "The group chat id", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @PathVariable("chat-id") String chatId,
        @Valid @ModelAttribute GroupChatUpdateResource resource
    ) {
        final Boolean isUpdated = chatService.updateGroupChat(chatId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @Operation(
        summary = "Delete an existing chat",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Chat deleted successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Unable to delete the chat",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @DeleteMapping(value = "/{chat-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> deleteGroupChat(
        @Schema(description = "The group chat id", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @PathVariable("chat-id") String chatId
    ) {
        final Boolean isDeleted = chatService.deleteGroupChat(chatId);
        return ResponseEntity.ok(new BooleanResponse(isDeleted));
    }
}
