package com.project.media.services.impl;

import com.project.media.domain.dto.MediaResourceDto;
import com.project.media.domain.models.Media;
import com.project.media.exceptions.MediaNotFoundException;
import com.project.media.repositories.CustomMediaRepository;
import com.project.media.repositories.MediaRepository;
import com.project.media.services.MediaService;
import com.project.media.utils.FileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.List;
import java.util.UUID;

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
    public Mono<String> saveMedia(
        @NonNull FilePart filePart,
        @NonNull String relativePath,
        @NonNull String entityId
    ) {
        return FileUtils.saveLocalFile(filePart, mediaBasePath, generateMediaReferencePath(relativePath))
            .flatMap(fileName -> {
                Media media = Media.builder()
                    .id(generateId())
                    .entityId(entityId)
                    .name(fileName)
                    .size(filePart.headers().getContentLength())
                    .reference(relativePath)
                    .isDeleted(false)
                    .build();
                return mediaRepository.save(media).map(Media::getId);
            });
    }

    @Override
    public Mono<List<String>> getMediaList(String entityId) {
        return mediaRepository.findMediaByEntityId(entityId)
            .map(Media::getId)
            .collectList()
            .flatMap(list -> list.isEmpty() ? Mono.empty() : Mono.just(list))
            .onErrorResume(e -> {
                log.error("Error fetching media content for entityId: {}", entityId, e);
                return Mono.error(new MediaNotFoundException("Failed to fetch media content", e));
            });
    }

    @Override
    public Boolean canGenerateMediaUrl() {
        return true;
    }

    @Override
    public Mono<MediaResourceDto> getMediaView(String reference) {
        return mediaRepository.findMediaById(reference)
            .mapNotNull(media -> {
                Resource resource = FileUtils.getLocalFileResource(
                    mediaBasePath, generateMediaReferencePath(media.getReference()), media.getName()
                );
                return new MediaResourceDto(resource, resolveMediaType(media.getName()));
            }).onErrorResume(e -> {
                log.error("Error fetching media content for reference: {}", reference, e);
                return Mono.error(new MediaNotFoundException("Failed to fetch media content", e));
            });
    }

    @Override
    public Mono<Boolean> deleteMedia(String reference) {
        try {
            return customMediaRepository.deleteMediaById(reference);
        } catch (Exception e) {
            log.error("Error deleting media content for reference: {}", reference, e);
            return Mono.just(false);
        }
    }

    private MediaType resolveMediaType(String fileName) {
        String ext = FileUtils.extractFileExtension(fileName).toLowerCase();

        return switch (ext) {
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "mp4" -> MediaType.valueOf("video/mp4");
            case "webm" -> MediaType.valueOf("audio/webm");
            case "pdf" -> MediaType.APPLICATION_PDF;
            case "txt" -> MediaType.TEXT_PLAIN;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    private String generateMediaReferencePath(String dbReference) {
        return dbReference.replace(",", File.separator);
    }

    private String generateId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 24);
    }
}