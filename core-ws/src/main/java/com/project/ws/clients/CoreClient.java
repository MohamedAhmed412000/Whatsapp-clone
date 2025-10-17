package com.project.ws.clients;

import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CoreClient {
    ResponseEntity<List<String>> getSingleChatsUserIds();
}
