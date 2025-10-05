package com.project.core.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Schema(
    name = "ChatMessageResponse",
    description = "Schema to hold the chat messages response"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageResponse {
    Map<LocalDate, List<MessageResponse>> messages;
}
