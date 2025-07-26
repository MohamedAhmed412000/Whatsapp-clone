package com.project.whatsapp.rest.outbound;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatUserResponse {
    private String id;
    private String fullname;
    private String imageUrl;
    private boolean isAdmin;
}
