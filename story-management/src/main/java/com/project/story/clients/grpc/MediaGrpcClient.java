package com.project.story.clients.grpc;

import com.google.protobuf.ByteString;
import com.project.story.clients.MediaClient;
import com.project.story.clients.dto.inbound.MediaUploadResource;
import com.project.story.clients.dto.outbound.MediaUploadResponse;
import com.project.story.grpc.DeleteMediaResource;
import com.project.story.grpc.DeleteMediaResponse;
import com.project.story.grpc.MediaManageGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Component
@ConditionalOnProperty(name = "media.service.grpc.enabled", havingValue = "true")
public class MediaGrpcClient implements MediaClient {
    private final MediaManageGrpc.MediaManageBlockingStub grpcClient;

    @Override
    public ResponseEntity<MediaUploadResponse> saveMediaList(MediaUploadResource resource) {
        try {
            com.project.story.grpc.MediaUploadResponse grpcResponse = grpcClient.uploadMedia(
                com.project.story.grpc.MediaUploadResource.newBuilder()
                    .setEntityId(resource.getEntityId())
                    .setFilePath(resource.getFilePath())
                    .addAllFileNames(resource.getFiles().stream()
                        .map(MultipartFile::getOriginalFilename)
                        .toList())
                    .addAllFiles(resource.getFiles().stream().map(file -> {
                        try {
                            return ByteString.copyFrom(file.getBytes());
                        } catch (IOException e) {
                            throw new RuntimeException("Error reading file: " + file
                                .getOriginalFilename(), e);
                        }
                    }).toList())
                    .build()
            );

            return ResponseEntity.ok(new MediaUploadResponse(grpcResponse.getReferencesList()));
        } catch (StatusRuntimeException ex) {
            HttpStatus status = ex.getStatus().getCode().equals(io.grpc.Status.Code.NOT_FOUND) ?
                HttpStatus.NOT_FOUND : HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(status).body(null);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Override
    public void deleteMediaFile(String reference) {
        DeleteMediaResponse grpcResponse = grpcClient.deleteMediaFile(
            DeleteMediaResource.newBuilder().setReference(reference).build()
        );
        if (!grpcResponse.getIsDone())
            log.error("Delete media file failed with reference: {}", reference);
    }

    public MediaGrpcClient(
        @Value("${media.service.grpc.host}") String grpcHost,
        @Value("${media.service.grpc.port}") int port
    ) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(grpcHost, port)
            .usePlaintext()
            .build();
        this.grpcClient = MediaManageGrpc.newBlockingStub(channel);
    }
}
