package com.project.story.grpc;

import com.google.protobuf.Timestamp;
import com.project.story.domain.dto.StoryDetailsDto;
import com.project.story.domain.enums.StoryTypeEnum;
import com.project.story.rest.outbound.UserStoriesListResponse;
import com.project.story.services.StoryService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@GrpcService
@RequiredArgsConstructor
public class StoryGRPCService extends StoryManageGrpc.StoryManageImplBase {
    private final StoryService storyService;

    @Override
    public void getStoriesList(StoriesListResource request, StreamObserver<StoriesListResponse> responseObserver) {
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
            request.getUserId(), ""
        ));
        UserStoriesListResponse userStoriesListResponse = storyService.getUserStories();
        StoriesListResponse response = StoriesListResponse.newBuilder()
            .addAllStoriesList(userStoriesListResponse.getStoriesList().stream()
                .map(this::toStoryDetailsResponse)
                .toList())
            .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private StoryDetailsResponse toStoryDetailsResponse(StoryDetailsDto storyDetailsDto) {
        return StoryDetailsResponse.newBuilder()
            .setId(storyDetailsDto.getId())
            .setContent(storyDetailsDto.getStoryType().equals(StoryTypeEnum.TEXT)?
                storyDetailsDto.getContent(): "")
            .setMediaReference(storyDetailsDto.getStoryType().equals(StoryTypeEnum.MEDIA)?
                storyDetailsDto.getMediaReference(): "")
            .setStoryType(StoryTypeEnum.getProtoEnum(storyDetailsDto.getStoryType()))
            .setCreatedAt(convertToTimestamp(storyDetailsDto.getCreatedAt()))
            .build();
    }

    public Timestamp convertToTimestamp(LocalDateTime localDateTime) {
        return Timestamp.newBuilder()
            .setSeconds(localDateTime.toEpochSecond(ZoneOffset.UTC))
            .setNanos(localDateTime.getNano())
            .build();
    }
}
