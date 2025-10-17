package com.project.ws.clients.grpc;

import com.project.ws.clients.CoreClient;
import com.project.ws.grpc.ChatsUserIdsResource;
import com.project.ws.grpc.ChatsUserIdsResponse;
import com.project.ws.grpc.CoreManageGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name = "core.service.grpc.enabled", havingValue = "true")
public class CoreGrpcClient implements CoreClient {
    private final CoreManageGrpc.CoreManageBlockingStub grpcClient;

    @Override
    public ResponseEntity<List<String>> getSingleChatsUserIds() {
        try {
            ChatsUserIdsResponse response = grpcClient.getChatsUserIds(ChatsUserIdsResource.newBuilder()
                .setUserId(getUserId())
                .build());
            return ResponseEntity.ok(response.getUserIdList().stream().toList());
        } catch (StatusRuntimeException ex) {
            HttpStatus status = ex.getStatus().getCode().equals(io.grpc.Status.Code.NOT_FOUND) ?
                HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    private String getUserId() {
        return SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal().toString();
    }

    public CoreGrpcClient(
        @Value("${core.service.grpc.host}") String grpcHost,
        @Value("${core.service.grpc.port}") int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, port)
            .usePlaintext()
            .build();
        this.grpcClient = CoreManageGrpc.newBlockingStub(channel);
    }
}
