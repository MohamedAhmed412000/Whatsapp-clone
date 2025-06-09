package com.project.whatsapp.clients.dto.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaContentResponse {
    private String name;
    private Long size;
    private byte[] data;
}
