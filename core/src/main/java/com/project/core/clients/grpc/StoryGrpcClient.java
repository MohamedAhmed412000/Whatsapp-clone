package com.project.core.clients.grpc;

import com.google.protobuf.Timestamp;
import com.project.core.clients.StoryClient;
import com.project.core.clients.dto.inbound.UserStoriesListResource;
import com.project.core.clients.dto.outbound.StoryDetailsDto;
import com.project.core.clients.dto.outbound.UserStoriesListResponse;
import com.project.core.domain.enums.StoryTypeEnum;
import com.project.core.grpc.StoriesListResource;
import com.project.core.grpc.StoriesListResponse;
import com.project.core.grpc.StoryManageGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Slf4j
@Component
@ConditionalOnProperty(name = "story.service.grpc.enabled", havingValue = "true")
public class StoryGrpcClient implements StoryClient {
    private final StoryManageGrpc.StoryManageBlockingStub grpcClient;

    @Override
    public UserStoriesListResponse saveStoriesList(UserStoriesListResource resource) {
        StoriesListResponse grpcResponse = grpcClient.getStoriesList(StoriesListResource.newBuilder()
            .setUserId(resource.getUserId())
            .build()
        );
        List<StoryDetailsDto> storyDetailsDtoList = grpcResponse.getStoriesListList().stream().map(response ->
            StoryDetailsDto.builder()
                .id(response.getId())
                .content(response.getStoryType().equals(com.project.core.grpc.StoryTypeEnum.TEXT)?
                    response.getContent(): null)
                .mediaReference(response.getStoryType().equals(com.project.core.grpc.StoryTypeEnum.MEDIA)?
                    response.getMediaReference(): null)
                .storyType(StoryTypeEnum.getEnum(response.getStoryType()))
                .createdAt(convertToLocalDateTime(response.getCreatedAt()))
                .build()
        ).toList();
        return new UserStoriesListResponse(storyDetailsDtoList);
    }

    public StoryGrpcClient(
        @Value("${story.service.grpc.host}") String grpcHost,
        @Value("${story.service.grpc.port}") int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, port)
            .usePlaintext()
            .build();
        this.grpcClient = StoryManageGrpc.newBlockingStub(channel);
    }

    private LocalDateTime convertToLocalDateTime(Timestamp timestamp) {
        return LocalDateTime.ofInstant(
            Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos()),
            ZoneOffset.UTC
        );
    }
}
