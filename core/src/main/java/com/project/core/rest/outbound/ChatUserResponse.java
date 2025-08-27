package com.project.core.rest.outbound;

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
    private String imageFileReference;
    private boolean isAdmin;
}
