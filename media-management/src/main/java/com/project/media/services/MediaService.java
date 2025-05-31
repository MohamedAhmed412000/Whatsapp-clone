package com.project.media.services;

import com.project.media.rest.inbound.MediaContentResource;
import com.project.media.rest.outbound.MediaListResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface MediaService {
    String saveMedia(MultipartFile file, String relativePath, String entityId);
    Map<String, MediaListResponse> getMediaList(MediaContentResource mediaList);
}
