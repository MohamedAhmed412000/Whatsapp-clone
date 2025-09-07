package com.project.user.controllers;

import com.project.commons.rest.outbound.BooleanResponse;
import com.project.commons.rest.outbound.StringResponse;
import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.user.rest.inbound.UserContactCreationResource;
import com.project.user.rest.inbound.UserContactUpdateResource;
import com.project.user.services.UserContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
    name = "User Contacts Controller",
    description = "CRUD Rest APIs for managing user contacts"
)
@RestController
@RequestMapping("/api/v1/user-contacts")
@RequiredArgsConstructor
public class UserContactsController {

    private final UserContactService userContactService;

    @Operation(
        summary = "Create a new user contact",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contact created successfully",
                content = @Content(schema = @Schema(implementation = StringResponse.class))
            )
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> addNewContact(
        @Valid @RequestBody UserContactCreationResource resource
    ) {
        Long contactId = userContactService.addNewContact(resource);
        return ResponseEntity.ok(new StringResponse(contactId.toString()));
    }

    @Operation(
        summary = "Update contact details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contact details updated successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Contact not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{user-id}/update-contact", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateContact(
        @PathVariable("user-id") String userId,
        @Valid @RequestBody UserContactUpdateResource resource
    ) {
        boolean isDone = userContactService.updateContact(userId, resource);
        return ResponseEntity.ok(new BooleanResponse(isDone));
    }

    @Operation(
        summary = "Pin an existing contact",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contact pinned successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Contact not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{user-id}/pin-contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> pinContact(
        @PathVariable("user-id") String userId
    ) {
        boolean isDone = userContactService.pinContact(userId);
        return ResponseEntity.ok(new BooleanResponse(isDone));
    }

    @Operation(
        summary = "Unpin an existing contact",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contact unpinned successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Contact not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{user-id}/unpin-contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> unpinContact(
        @PathVariable("user-id") String userId
    ) {
        boolean isDone = userContactService.unpinContact(userId);
        return ResponseEntity.ok(new BooleanResponse(isDone));
    }

    @Operation(
        summary = "Block an existing contact",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contact blocked successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Contact not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{user-id}/block-contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> blockContact(
        @PathVariable("user-id") String userId
    ) {
        boolean isDone = userContactService.blockContact(userId);
        return ResponseEntity.ok(new BooleanResponse(isDone));
    }

    @Operation(
        summary = "Unblock an existing contact",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Contact unblocked successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Contact not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{user-id}/unblock-contact", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> unblockContact(
        @PathVariable("user-id") String userId
    ) {
        boolean isDone = userContactService.unblockContact(userId);
        return ResponseEntity.ok(new BooleanResponse(isDone));
    }
}
