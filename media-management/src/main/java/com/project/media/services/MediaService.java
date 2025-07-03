package com.project.media.services;

import com.project.media.rest.inbound.MediaContentResource;
import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.rest.outbound.MediaListResponse;
import org.springframework.data.util.Pair;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MediaService {
    Mono<String> saveMedia(MultipartFile file, String relativePath, String entityId);
    Flux<MediaContentResponse> getMediaContent(String entityId);
    Flux<Pair<String, MediaListResponse>> getMediaList(MediaContentResource mediaList);
}
