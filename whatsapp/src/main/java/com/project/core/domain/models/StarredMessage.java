package com.project.core.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

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
    private String id = UUID.randomUUID().toString();
    @Field(value = "message_id", targetType = FieldType.INT64)
    private Long messageId;
    @Field(value = "user_id")
    private String userId;
}
