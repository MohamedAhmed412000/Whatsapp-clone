package com.project.whatsapp.clients.dto.outbound;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MediaContentResponse {
    private String name;
    private Long size;
    private byte[] data;
}
