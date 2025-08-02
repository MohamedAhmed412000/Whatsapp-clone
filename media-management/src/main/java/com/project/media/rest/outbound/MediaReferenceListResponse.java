package com.project.media.rest.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MediaReferenceListResponse {
    private List<String> mediaReferencesList;
}
