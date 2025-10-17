package com.project.ws.clients.rest;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConditionalOnProperty(name = "core.service.grpc.enabled", havingValue = "false")
public class CoreClientFallback implements CoreFeignClient {
    @Override
    public ResponseEntity<List<String>> getSingleChatsUserIds() {
        return ResponseEntity.internalServerError().body(null);
    }
}
