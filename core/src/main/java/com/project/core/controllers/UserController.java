package com.project.core.controllers;

import com.project.core.rest.inbound.UserUpdateResource;
import com.project.core.rest.outbound.BooleanResponse;
import com.project.core.rest.outbound.UserResponse;
import com.project.core.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getUsers(
        @RequestParam(value = "q", required = false) String query
    ) {
        return ResponseEntity.ok(userService.getUsers(query));
    }

    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> getUser() {
        return ResponseEntity.ok(userService.getMyUserDetails());
    }

    @PatchMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BooleanResponse> updateUser(
        @Valid @ModelAttribute UserUpdateResource resource
    ) {
        boolean isUpdated = userService.updateUserDetails(resource);
        return ResponseEntity.ok(new BooleanResponse(isUpdated));
    }
}
