package com.project.media.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Document(collection = "media")
public class Media extends BaseModel {
    @MongoId
    @Field(value = "_id")
    private String id;
    @Field(value = "entity_id")
    private String entityId;
    @Field(value = "name")
    private String name;
    @Field(value = "size", targetType = FieldType.INT64)
    private Long size;
    @Field(value = "reference")
    private String reference;
    @Field(value = "is_deleted", targetType = FieldType.BOOLEAN)
    private Boolean isDeleted;
}
