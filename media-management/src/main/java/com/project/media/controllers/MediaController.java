package com.project.media.controllers;

import com.project.commons.rest.outbound.BooleanResponse;
import com.project.commons.rest.outbound.dto.ErrorBody;
import com.project.media.rest.outbound.MediaReferenceListResponse;
import com.project.media.services.MediaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(
    name = "Media Controller",
    description = "CRUD Rest APIs for managing media files"
)
@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
public class MediaController {

    private final MediaService mediaService;

    @Operation(
        summary = "Upload media file",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Media file uploaded successfully",
                content = @Content(schema = @Schema(implementation = MediaReferenceListResponse.class))
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Media file creation failed",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<MediaReferenceListResponse>> uploadMedia(
        @Schema(description = "The media files")
        @NotNull @RequestPart("files") Flux<FilePart> files,
        @Schema(description = "The media file path", example = "550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @RequestPart("filePath") String filePath,
        @Schema(description = "The entity Id related to the media", example = "USR_550e8400-e29b-41d4-a716-446655440000")
        @NotEmpty @RequestPart("entityId") String entityId
    ) {
        return files.flatMap(filePart -> mediaService.saveMedia(filePart, filePath, entityId))
            .collectList()
            .map(MediaReferenceListResponse::new)
            .map(ResponseEntity::ok);
    }

    @Operation(
        summary = "Retrieve entity related media references",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Entity media references retrieved successfully",
                content = @Content(schema = @Schema(implementation = MediaReferenceListResponse.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Entity media files not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @GetMapping(value = "/list/{entityId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<MediaReferenceListResponse>> getMediaListReferences(
        @NotEmpty @PathVariable String entityId
    ) {
        return mediaService.getMediaList(entityId)
            .map(MediaReferenceListResponse::new)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "View media file",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Media file viewed successfully",
                content = @Content(schema = @Schema(implementation = Resource.class))
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Media file not found",
                content = @Content(schema = @Schema(implementation = ErrorBody.class))
            )
        }
    )
    @GetMapping(value = "/{reference}")
    public Mono<ResponseEntity<Resource>> getMediaView(
        @NotEmpty @PathVariable String reference
    ) {
        return Mono.just(mediaService.canGenerateMediaUrl())
            .flatMap(canGenerate -> {
                if (canGenerate) {
                    return mediaService.getMediaView(reference).map(mediaResourceDto ->
                        ResponseEntity.ok()
                            .contentType(mediaResourceDto.getMediaType())
                            .body(mediaResourceDto.getResource())
                    );
                } else {
                    return Mono.just(ResponseEntity.badRequest().body(null));
                }
            });
    }

    @Operation(
        summary = "Delete media file",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Media file deleted successfully",
                content = @Content(schema = @Schema(implementation = BooleanResponse.class))
            )
        }
    )
    @DeleteMapping(value = "/{reference}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<BooleanResponse>> deleteMedia(
        @NotEmpty @PathVariable String reference
    ) {
        return mediaService.deleteMedia(reference)
            .map(BooleanResponse::new)
            .map(ResponseEntity::ok);
    }
}