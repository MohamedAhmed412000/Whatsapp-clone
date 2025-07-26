package com.project.media.services.impl;

import com.project.media.domain.models.Media;
import com.project.media.mappers.MediaMapper;
import com.project.media.repositories.CustomMediaRepository;
import com.project.media.repositories.MediaRepository;
import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.services.MediaService;
import com.project.media.utils.FileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "application.file.uploads.media-storage-type", havingValue = "local")
public class LocalMediaServiceImpl implements MediaService {

    @Value("${application.file.uploads.media-output-path}")
    private String mediaBasePath;
    private final MediaRepository mediaRepository;
    private final CustomMediaRepository customMediaRepository;

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
            .isDeleted(false)
            .build();
        return mediaRepository.save(media).map(Media::getReference);
    }

    @Override
    public Flux<MediaContentResponse> getMediaList(String entityId) {
        return mediaRepository.findMediaByEntityId(entityId)
            .map(media -> MediaMapper.mapToResponse(media, mediaBasePath))
            .onErrorResume(e -> {
                log.error("Error fetching media content for entityId: {}", entityId, e);
                return Mono.error(new RuntimeException("Failed to fetch media content", e));
            });
    }

    @Override
    public Mono<MediaContentResponse> getMedia(String reference) {
        return mediaRepository.findMediaByReference(reference)
            .map(media -> MediaMapper.mapToResponse(media, mediaBasePath))
            .onErrorResume(e -> {
                log.error("Error fetching media content for reference: {}", reference, e);
                return Mono.error(new RuntimeException("Failed to fetch media content", e));
            });
    }

    @Override
    public Boolean canGenerateMediaUrl() {
        return false;
    }

    @Override
    public Mono<String> getMediaUrl(String reference) {
        return Mono.just("Media URL generation isn't available for this storage type.");
    }

    @Override
    public Boolean deleteMedia(String reference) {
        try {
            customMediaRepository.deleteMediaByReference(reference);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}