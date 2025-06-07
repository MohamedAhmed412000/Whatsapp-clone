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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LocalMediaServiceImpl implements MediaService {

    @Value("${application.file.uploads.media-output-path}")
    private String mediaBasePath;
    private final MediaRepository mediaRepository;

    @Override
    public String saveMedia(@NonNull MultipartFile file, @NonNull String relativePath, @NonNull String entityId) {
        final String reference = FileUtils.saveLocalFile(file, mediaBasePath, relativePath);
        Media media = Media.builder()
            .entityId(entityId)
            .name(file.getOriginalFilename())
            .size(file.getSize())
            .reference(reference)
            .build();
        mediaRepository.save(media);
        return reference;
    }

    @Override
    public MediaListResponse getMediaContent(String entityId) {
        List<MediaContentResponse> responseList = mediaRepository.findMediaByEntityId(entityId).stream().map(
            media -> MediaMapper.mapToResponse(media, mediaBasePath)
        ).toList();
        return new MediaListResponse(responseList);
    }

    @Override
    public Map<String, MediaListResponse> getMediaList(MediaContentResource mediaList) {
        return mediaList.getEntityIds().stream()
            .collect(Collectors.toMap(
                entityId -> entityId,
                entityId -> {
                    List<MediaContentResponse> responses = mediaRepository.findMediaByEntityId(
                        entityId).stream().map(
                        media -> MediaMapper.mapToResponse(media, mediaBasePath)
                    ).toList();
                    return new MediaListResponse(responses);
                }
            ));
    }

}
