package com.project.whatsapp.controllers;

import com.project.whatsapp.constants.Headers;
import com.project.whatsapp.rest.outbound.UserResponse;
import com.project.whatsapp.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserResponse>> getUsers(
        @Parameter(hidden = true)
        @RequestHeader(name = Headers.USER_ID_HEADER) String userIdHeader
    ) {
        return ResponseEntity.ok(userService.getUsers(userIdHeader));
    }
}
