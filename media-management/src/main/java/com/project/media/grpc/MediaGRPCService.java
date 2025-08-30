package com.project.media.grpc;

import com.google.protobuf.ByteString;
import com.project.media.services.MediaService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.grpc.server.service.GrpcService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

@GrpcService
@RequiredArgsConstructor
public class MediaGRPCService extends MediaManageGrpc.MediaManageImplBase {
    private final MediaService mediaService;

    @Override
    public void uploadMedia(MediaUploadResource request, StreamObserver<MediaUploadResponse> responseObserver) {
        Flux.range(0, Math.min(request.getFilesCount(), request.getFileNamesCount()))
            .flatMap(i -> mediaService.saveMedia(
                    createFilePart(request.getFileNames(i), request.getFiles(i)),
                    request.getFilePath(),
                    request.getEntityId()
                )
            ).collectList()
            .map(references -> MediaUploadResponse.newBuilder()
                .addAllReferences(references)
                .build()
            ).subscribe(response -> {
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                },
                responseObserver::onError
            );
    }

    @Override
    public void getMediaReferencesList(MediaReferencesListEntityIdResource request, StreamObserver<MediaReferencesListEntityIdResponse> responseObserver) {
        mediaService.getMediaList(request.getEntityId())
            .switchIfEmpty(Mono.defer(() -> {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("Media not found for entityId: " + request.getEntityId())
                        .asRuntimeException()
                );
                return Mono.empty();
            }))
            .map(references -> MediaReferencesListEntityIdResponse.newBuilder()
                .addAllReference(references)
                .build()
            ).subscribe(response -> {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }, throwable -> {
                if (!(throwable instanceof StatusRuntimeException)) {
                    responseObserver.onError(
                        Status.INTERNAL
                            .withDescription("Unexpected error occurred")
                            .withCause(throwable)
                            .asRuntimeException()
                    );
                } else {
                    responseObserver.onError(throwable);
                }
            });
    }

    @Override
    public void viewMediaFile(ViewMediaResource request, StreamObserver<ViewMediaResponse> responseObserver) {
        mediaService.getMediaView(request.getReference())
            .switchIfEmpty(Mono.defer(() -> {
                responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("Media not found for reference: " + request.getReference())
                        .asRuntimeException()
                );
                return Mono.empty();
            }))
            .<ViewMediaResponse>handle(
                (mediaResourceDto, sink) -> {
                try {
                    sink.next(ViewMediaResponse.newBuilder()
                        .setData(ByteString.copyFrom(mediaResourceDto.getResource().getContentAsByteArray()))
                        .build());
                } catch (IOException e) {
                    sink.error(new RuntimeException("Error reading media content", e));
                }
            }).subscribe(response -> {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                }, throwable -> {
                    if (!(throwable instanceof StatusRuntimeException)) {
                        responseObserver.onError(
                            Status.INTERNAL
                                .withDescription("Unexpected error occurred")
                                .withCause(throwable)
                                .asRuntimeException()
                        );
                    } else {
                        responseObserver.onError(throwable);
                    }
            });
    }

    @Override
    public void deleteMediaFile(DeleteMediaResource request, StreamObserver<DeleteMediaResponse> responseObserver) {
        mediaService.deleteMedia(request.getReference())
            .map(isDone -> DeleteMediaResponse.newBuilder()
                .setIsDone(isDone)
                .build()
            )
            .subscribe(response -> {
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }, responseObserver::onError);
    }

    private FilePart createFilePart(String fileName, ByteString fileContent) {
        return new FilePart() {
            @Override
            public @NonNull String filename() {
                return fileName;
            }

            @Override
            public @NonNull Mono<Void> transferTo(@NonNull Path dest) {
                return DataBufferUtils.write(content(), dest, StandardOpenOption.CREATE)
                    .then();
            }

            @Override
            public @NonNull String name() {
                return fileName;
            }

            @Override
            public @NonNull Flux<DataBuffer> content() {
                DataBuffer buffer = new DefaultDataBufferFactory().wrap(fileContent.toByteArray());
                return Flux.just(buffer);
            }

            @Override
            public @NonNull HttpHeaders headers() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                return headers;
            }
        };
    }
}
