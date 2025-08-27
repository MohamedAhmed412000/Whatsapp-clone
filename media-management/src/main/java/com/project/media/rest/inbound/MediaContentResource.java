package com.project.media.rest.inbound;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@Validated
public class MediaContentResource {
    @NotEmpty
    private List<String> entityIds;
}
