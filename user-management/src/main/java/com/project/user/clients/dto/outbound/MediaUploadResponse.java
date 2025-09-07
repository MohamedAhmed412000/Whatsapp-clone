package com.project.user.clients.dto.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaUploadResponse {
    private List<String> mediaReferencesList;
}
