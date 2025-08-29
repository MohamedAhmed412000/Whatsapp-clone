package com.project.core.rest.inbound;

import com.project.core.domain.enums.MessageTypeEnum;
import com.project.core.validators.ValidResourceWhenCreateMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(
    name = "MessageCreationResource",
    description = "Schema to hold the message creation data"
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
@ValidResourceWhenCreateMessage
public class MessageResource {
    @Schema(description = "The message string content [Mandatory in case messageType=TEXT]", example = "Hello")
    private String content;
    @NotNull
    @Schema(description = "The message content type", allowableValues = {
        "TEXT", "MEDIA", "AUDIO"
    })
    private MessageTypeEnum messageType;
    @NotEmpty
    @Schema(description = "The chat Id to add the message to it", example = "550e8400-e29b-41d4-a716-446655440000")
    private String chatId;
    @Schema(description = "Flag to specify if the message is forwarded from another chat")
    private Boolean isForwarded;
    @Schema(description = "The message Id of the source replied message", example = "1755250373935")
    private Long repliedMessageId;
    @Schema(description = "List of media files", format = "binary")
    private List<MultipartFile> mediaFiles;
}
