package com.project.media.rest.inbound;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Schema(
    name = "MediaContentResource",
    description = "Schema to hold the chat-user update data"
)
@Data
@AllArgsConstructor
@Validated
public class MediaContentResource {
    @NotEmpty
    private List<String> entityIds;
}
