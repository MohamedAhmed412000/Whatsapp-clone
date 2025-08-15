package com.project.media.services.impl;

import com.cloudinary.Cloudinary;
import com.project.media.domain.dto.MediaResourceDto;
import com.project.media.services.MediaService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "application.file.uploads.media-storage-type", havingValue = "cloudinary")
public class CloudinaryMediaServiceImpl implements MediaService {

    @Value("${application.file.uploads.cloudinary-media-path}")
    private String cloudinaryFolderRoot;
    private final Cloudinary cloudinary;

    @Override
    public Mono<String> saveMedia(FilePart filePart, String relativePath, String entityId) {
        return null;
    }

    @Override
    public Mono<List<String>> getMediaList(String entityId) {
        return null;
    }

    @Override
    public Boolean canGenerateMediaUrl() {
        return true;
    }

    @Override
    public Mono<MediaResourceDto> getMediaView(String reference) {
        return null;
    }

    @Override
    public Mono<Boolean> deleteMedia(String reference) {
        return null;
    }
}
