package com.project.media.mappers;

import com.project.media.domain.models.Media;
import com.project.media.rest.outbound.MediaContentResponse;
import com.project.media.utils.FileUtils;

public class MediaMapper {

    public static MediaContentResponse mapToResponse(Media media, String mediaBasePath) {
        MediaContentResponse response = new MediaContentResponse();
        response.setName(media.getName());
        response.setSize(media.getSize());
        response.setData(FileUtils.readLocalFile(mediaBasePath, media.getReference()));
        return response;
    }

}
