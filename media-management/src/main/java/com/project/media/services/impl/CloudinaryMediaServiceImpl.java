package com.project.media.services.impl;

import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.services.MediaService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@ConditionalOnProperty(name = "application.file.uploads.media-storage-type", havingValue = "cloudinary")
public class CloudinaryMediaServiceImpl implements MediaService {
    @Override
    public Mono<String> saveMedia(MultipartFile file, String relativePath, String entityId) {
        return null;
    }

    @Override
    public Flux<MediaContentResponse> getMediaList(String entityId) {
        return null;
    }

    @Override
    public Mono<MediaContentResponse> getMedia(String reference) {
        return null;
    }

    @Override
    public Boolean canGenerateMediaUrl() {
        return true;
    }

    @Override
    public Mono<String> getMediaUrl(String reference) {
        return null;
    }

    @Override
    public Boolean deleteMedia(String reference) {
        return null;
    }
}
