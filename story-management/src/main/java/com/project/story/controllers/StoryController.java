package com.project.story.controllers;

import com.project.commons.rest.outbound.BooleanResponse;
import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.commons.rest.outbound.StringResponse;
import com.project.story.domain.dto.StoryDetailsDto;
import com.project.story.rest.inbound.StoryUpdateResource;
import com.project.story.rest.inbound.StoryCreationResource;
import com.project.story.rest.outbound.UserStoriesListResponse;
import com.project.story.services.StoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(
    name = "User Stories Controller",
    description = "CRUD Rest APIs for managing user-stories"
)
@RestController
@RequestMapping("/api/v1/user-stories")
@RequiredArgsConstructor
public class StoryController {

    private final StoryService storyService;

    @Operation(
        summary = "Add new story",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Story added successfully",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = StringResponse.class)))
            )
        }
    )
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StringResponse> createStory(
        @Valid @RequestBody StoryCreationResource resource
    ) {
        Long storyId = storyService.createStory(resource);
        return ResponseEntity.ok(new StringResponse(storyId.toString()));
    }

    @Operation(
        summary = "Retrieve my stories",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Get my stories successfully",
                content = @Content(schema = @Schema(implementation = UserStoriesListResponse.class))
            )
        }
    )
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserStoriesListResponse> getMyStories() {
        return ResponseEntity.ok(storyService.getUserStories());
    }

    @Operation(
        summary = "Retrieve my contacts stories"
    )
    @GetMapping(value = "/contacts", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, List<StoryDetailsDto>>> getMyContactsStories() {
        return ResponseEntity.ok(storyService.getUserContactsStories().getContactsStoriesList());
    }

    @Operation(
        summary = "Update story content",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Story content updated successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Story not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Update action not allowed",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PatchMapping(value = "/{user-story-id}", consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateStory(
        @Schema(description = "The user story id", example = "1755250373935")
        @NotNull @PathVariable("user-story-id") Long userStoryId,
        @Valid @RequestBody StoryUpdateResource resource
    ) {
        boolean isUpdated = storyService.updateStory(userStoryId, resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }

    @Operation(
        summary = "Delete an existing user story",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Story deleted successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Story not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @DeleteMapping(value = "/{user-story-id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> deleteStory(
        @Schema(description = "The user story id", example = "1755250373935")
        @NotNull @PathVariable("user-story-id") Long chatId
    ) {
        final boolean isDeleted = storyService.deleteStory(chatId);
        return ResponseEntity.ok(new BooleanResponse(isDeleted));
    }
}
