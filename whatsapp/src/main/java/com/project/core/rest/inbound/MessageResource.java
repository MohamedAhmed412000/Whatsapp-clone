package com.project.core.rest.inbound;

import com.project.core.domain.enums.MessageTypeEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class MessageResource {
    private String content;
    @NotNull
    private MessageTypeEnum messageType;
    @NotEmpty
    private String chatId;
    private Boolean isForwarded;
    private Long repliedMessageId;
    private List<MultipartFile> mediaFiles;
}
