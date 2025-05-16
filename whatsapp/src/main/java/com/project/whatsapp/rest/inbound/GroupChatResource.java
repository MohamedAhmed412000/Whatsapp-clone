package com.project.whatsapp.rest.inbound;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GroupChatResource {
    @NotEmpty
    private String name;
    @NotEmpty
    private String senderId;
    @NotEmpty
    private List<String> receiversIds;
}
