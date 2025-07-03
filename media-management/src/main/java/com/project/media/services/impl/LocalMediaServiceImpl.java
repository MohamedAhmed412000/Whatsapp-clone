package com.project.media.services.impl;

import com.project.media.domain.models.Media;
import com.project.media.mappers.MediaMapper;
import com.project.media.repositories.MediaRepository;
import com.project.media.rest.inbound.MediaContentResource;
import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.rest.outbound.MediaListResponse;
import com.project.media.services.MediaService;
import com.project.media.utils.FileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalMediaServiceImpl implements MediaService {

    @Value("${application.file.uploads.media-output-path}")
    private String mediaBasePath;
    private final MediaRepository mediaRepository;

    @Override
    public Mono<String> saveMedia(@NonNull MultipartFile file, @NonNull String relativePath, @NonNull String entityId) {
        final String reference = FileUtils.saveLocalFile(file, mediaBasePath, relativePath);
        if (reference == null) {
            return Mono.error(new RuntimeException("Failed to save media file"));
        }
        Media media = Media.builder()
            .entityId(entityId)
            .name(file.getOriginalFilename())
            .size(file.getSize())
            .reference(reference)
            .build();
        return mediaRepository.save(media).map(Media::getReference);
    }

    @Override
    public Flux<MediaContentResponse> getMediaContent(String entityId) {
        return mediaRepository.findMediaByEntityId(entityId)
            .map(media -> MediaMapper.mapToResponse(media, mediaBasePath))
            .onErrorResume(e -> {
                log.error("Error fetching media content for entityId: {}", entityId, e);
                return Mono.error(new RuntimeException("Failed to fetch media content", e));
            });
    }

    @Override
    public Flux<Pair<String, MediaListResponse>> getMediaList(MediaContentResource mediaList) {
        if (mediaList == null || mediaList.getEntityIds() == null) {
            return Flux.error(new IllegalArgumentException("MediaList or entityIds cannot be null"));
        }
        
        return Flux.fromIterable(mediaList.getEntityIds())
            .flatMap(entityId -> mediaRepository.findMediaByEntityId(entityId)
                .map(media -> MediaMapper.mapToResponse(media, mediaBasePath))
                .collectList()
                .map(responses -> Pair.of(entityId, new MediaListResponse(responses)))
                .onErrorResume(e -> {
                    log.error("Error processing media for entityId: {}", entityId, e);
                    return Mono.error(new RuntimeException("Failed to process media for entityId: " + entityId, e));
                })
            );
    }

}