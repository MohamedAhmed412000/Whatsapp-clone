package com.project.whatsapp.clients.dto.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaListResponse {
    private List<MediaContentResponse> mediaContentResponses;
}
