package com.project.whatsapp.rest.inbound;

import com.project.whatsapp.domain.enums.MessageTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class MessageResource {
    private String content;
    private MessageTypeEnum messageType;
    private String chatId;
    private Boolean isForwarded;
    private Long repliedMessageId;
    private List<MultipartFile> mediaFiles;
}
