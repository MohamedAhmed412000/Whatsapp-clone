package com.project.core.controllers;

import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.core.rest.inbound.UserUpdateResource;
import com.project.core.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.UserResponse;
import com.project.core.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
    name = "Users Controller",
    description = "CRUD Rest APIs for managing users"
)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
        summary = "Retrieve searched users except me",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get searched users successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponse.class)))
            )
        }
    )
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getUsers(
        @Schema(description = "The users search query", example = "ha")
        @RequestParam(value = "q", required = false) String query
    ) {
        return ResponseEntity.ok(userService.getUsers(query));
    }

    @Operation(
        summary = "Retrieve my user details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "My user details retrieved successfully",
                content = @Content(schema = @Schema(implementation = UserResponse.class))
            )
        }
    )
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(userService.getMyUserDetails());
    }

    @Operation(
        summary = "Update my user details",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "My user details updated successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "User not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateUser(
        @Valid @ModelAttribute UserUpdateResource resource
    ) {
        boolean isUpdated = userService.updateUserDetails(resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }
}
