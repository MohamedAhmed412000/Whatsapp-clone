package com.project.ws.clients.rest;

import com.project.ws.clients.CoreClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(value = "core", dismiss404 = true, fallback = CoreClientFallback.class)
@ConditionalOnProperty(name = "core.service.grpc.enabled", havingValue = "false")
public interface CoreFeignClient extends CoreClient {
    @GetMapping(value = "/api/v1/chats/users", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<String>> getSingleChatsUserIds();
}
