package com.project.whatsapp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class MessageContent {
    @Field(value = "content")
    private String content;
    @Field(value = "media_references")
    private List<String> mediaReferences;
}
