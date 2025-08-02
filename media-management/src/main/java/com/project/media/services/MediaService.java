package com.project.media.services;

import com.project.media.domain.dto.MediaResourceDto;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MediaService {
    Mono<String> saveMedia(FilePart filePart, String relativePath, String entityId);
    Mono<List<String>> getMediaList(String entityId);
    Boolean canGenerateMediaUrl();
    Mono<MediaResourceDto> getMediaView(String reference);
    Mono<Boolean> deleteMedia(String reference);
}
