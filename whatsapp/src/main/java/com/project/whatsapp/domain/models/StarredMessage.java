package com.project.whatsapp.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "starred_message")
public class StarredMessage extends BaseModel {
    @MongoId
    @Field(value = "_id")
    private Long id;
    @Field(value = "message_id")
    private Long messageId;
    @Field(value = "user_id")
    private String userId;
}
