package com.project.media.services;

import com.project.media.rest.outbound.MediaContentResponse;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaService {
    Mono<String> saveMedia(MultipartFile file, String relativePath, String entityId);
    Flux<MediaContentResponse> getMediaList(String entityId);
    Mono<MediaContentResponse> getMedia(String reference);
    Boolean canGenerateMediaUrl();
    Mono<String> getMediaUrl(String reference);
    Boolean deleteMedia(String reference);
}
