package com.project.core.rest.outbound;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Schema(
    name = "ChatResponse",
    description = "Schema to hold the chat response"
)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatResponse {
    @Schema(description = "The chat Id", example = "550e8400-e29b-41d4-a716-446655440000")
    private String id;
    @Schema(description = "The chat name", example = "Friends")
    private String name;
    @Schema(description = "The bio of the chat", example = "...")
    private String description;
    @Schema(description = "Flag to describe if the chat is group-chat")
    private Boolean isGroupChat;
    @Schema(description = "The chat profile image reference", example = "407dd10ad78743e49d0b58e7")
    private String chatImageReference;
    @Schema(description = "The number of unread messages", example = "3")
    private long unreadCount;
    @Schema(description = "The last message sent in the chat", example = "Attachment")
    private String lastMessage;
    @Schema(description = "The last message time sent in the chat")
    private LocalDateTime lastMessageTime;
    @Schema(description = "Flag to determine if the another user in the single chat is online")
    private Boolean isRecipientOnline;
    @Schema(description = "The creator user id", example = "550e8400-e29b-41d4-a716-446655440000")
    private String senderId;
    @Schema(description = "The list of other users ids in the chat")
    private List<String> receiversId;
}
