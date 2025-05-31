package com.project.media.rest.inbound;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MediaContentResource {
    private List<String> entityIds;
}
