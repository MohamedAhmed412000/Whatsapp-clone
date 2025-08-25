package com.project.core.controllers;

import com.project.core.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.StarredMessageResponse;
import com.project.core.services.StarredMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/starred-messages")
@RequiredArgsConstructor
public class StarredMessageController {

    private final StarredMessageService starredMessageService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StarredMessageResponse>> getStarredMessages(
        @RequestParam("page") Integer page
    ) {
        return ResponseEntity.ok(starredMessageService.findStarredMessages(page));
    }

    @PostMapping(value = "/{message-id}/star-message")
    public ResponseEntity<BooleanResponse> starMessage(
        @PathVariable("message-id") Long messageId
    ) {
        boolean isStarred = starredMessageService.starMessage(messageId);
        return ResponseEntity.ok(new BooleanResponse(isStarred));
    }

    @PostMapping(value = "/{message-id}/unstar-message")
    public ResponseEntity<BooleanResponse> unstarMessage(
        @PathVariable("message-id") Long messageId
    ) {
        boolean isStarred = starredMessageService.unstarMessage(messageId);
        return ResponseEntity.ok(new BooleanResponse(isStarred));
    }
}
