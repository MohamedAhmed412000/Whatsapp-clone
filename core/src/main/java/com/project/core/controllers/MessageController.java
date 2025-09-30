package com.project.core.controllers;

import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.core.rest.inbound.MessageResource;
import com.project.core.rest.inbound.MessageUpdateResource;
import com.project.commons.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.ChatMessageResponse;
import com.project.core.rest.outbound.MessageCreationResponse;
import com.project.core.rest.outbound.MessageResponse;
import com.project.core.services.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
    name = "Messages Controller",
    description = "CRUD Rest APIs for managing messages"
)
@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(
        summary = "Create a message in chat",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Message created successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Chat not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageCreationResponse> saveMessage(@Valid @ModelAttribute MessageResource resource) {
        return ResponseEntity.ok(messageService.saveMessage(resource));
    }

    @Operation(
        summary = "Retrieve chat messages",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get paginated chat messages list",
                content = @Content(schema = @Schema(implementation = ChatMessageResponse.class))
            )
        }
    )
    @GetMapping(value = "/chat/{chatId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<LocalDate, List<MessageResponse>>> getChatMessages(
        @Schema(description = "The chat id", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @PathVariable("chatId") String chatId,
        @RequestParam("page") Integer page
    ) {
        return ResponseEntity.ok(messageService.findChatMessages(chatId, page));
    }

    @Operation(
        summary = "Edit an existing chat message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Message edited successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Unable to edit the message",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{message-id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> editMessage(
        @Schema(description = "The message Id", example = "1755250373935")
        @NotEmpty @PathVariable("message-id") Long messageId,
        @Valid @RequestBody MessageUpdateResource resource
    ) {
        boolean isUpdated = messageService.editMessage(messageId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @Operation(
        summary = "Delete an existing chat message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Message deleted successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Unable to delete the message",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @DeleteMapping(value = "/{message-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> deleteMessage(
        @Schema(description = "The message Id", example = "1755250373935")
        @NotEmpty @PathVariable("message-id") Long messageId,
        @RequestParam(value = "delete-for-everyone", defaultValue = "false") boolean deleteForEveryone
    ) {
        boolean isUpdated = messageService.deleteMessage(messageId, deleteForEveryone);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

}
