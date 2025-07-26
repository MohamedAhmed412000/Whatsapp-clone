package com.project.whatsapp.rest.inbound;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatCreationResource {
    @NotEmpty
    private String name;
    private String imageUrl;
    private String description;
    @NotEmpty(message = "At least one receiver id is required")
    private List<String> receiversIds;
}
