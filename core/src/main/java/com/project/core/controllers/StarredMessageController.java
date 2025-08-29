package com.project.core.controllers;

import com.project.core.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.StarredMessageResponse;
import com.project.core.services.StarredMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Starred Messages Controller",
    description = "CRUD Rest APIs for managing starred messages"
)
@RestController
@RequestMapping("/api/v1/starred-messages")
@RequiredArgsConstructor
public class StarredMessageController {

    private final StarredMessageService starredMessageService;

    @Operation(
        summary = "Retrieve paginated starred messages",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get paginated starred messages successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = StarredMessageResponse.class)))
            )
        }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StarredMessageResponse>> getStarredMessages(
        @RequestParam("page") Integer page
    ) {
        return ResponseEntity.ok(starredMessageService.findStarredMessages(page));
    }

    @Operation(
        summary = "Star an existing message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The existing message is starred successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            )
        }
    )
    @PostMapping(value = "/{message-id}/star-message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> starMessage(
        @Schema(description = "The message Id", example = "1755250373935")
        @NotEmpty @PathVariable("message-id") Long messageId
    ) {
        boolean isStarred = starredMessageService.starMessage(messageId);
        return ResponseEntity.ok(new BooleanResponse(isStarred));
    }

    @Operation(
        summary = "Unstar an existing message",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "The existing message is unstarred successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            )
        }
    )
    @PostMapping(value = "/{message-id}/unstar-message", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> unstarMessage(
        @Schema(description = "The message Id", example = "1755250373935")
        @NotEmpty @PathVariable("message-id") Long messageId
    ) {
        boolean isStarred = starredMessageService.unstarMessage(messageId);
        return ResponseEntity.ok(new BooleanResponse(isStarred));
    }
}
