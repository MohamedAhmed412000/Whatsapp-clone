package com.project.core.grpc;

import com.project.commons.filters.dto.CustomAuthentication;
import com.project.core.services.ChatService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@GrpcService
@RequiredArgsConstructor
public class CoreGRPCService extends CoreManageGrpc.CoreManageImplBase {
    private final ChatService chatService;

    @Override
    public void getChatsUserIds(ChatsUserIdsResource request, StreamObserver<ChatsUserIdsResponse> responseObserver) {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthentication(
            request.getUserId(), ""
        ));
        List<String> userIds = chatService.getSingleChatsUsers();
        responseObserver.onNext(ChatsUserIdsResponse.newBuilder()
            .addAllUserId(userIds)
            .build());
        responseObserver.onCompleted();
    }
}
