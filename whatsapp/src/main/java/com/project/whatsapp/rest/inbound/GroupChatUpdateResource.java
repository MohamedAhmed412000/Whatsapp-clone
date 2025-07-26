package com.project.whatsapp.rest.inbound;

import com.project.whatsapp.validators.AtLeastOneFieldNotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@AtLeastOneFieldNotEmpty
public class GroupChatUpdateResource {
    private String name;
    private String imageUrl;
    private String description;
}
