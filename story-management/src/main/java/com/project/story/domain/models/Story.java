package com.project.story.domain.models;

import com.project.story.domain.enums.StoryTypeEnum;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "story")
public class Story extends BaseModel {
    @MongoId
    @Field(value = "_id", targetType = FieldType.INT64)
    private Long id;
    @Field(value = "user_id")
    private String userId;
    @Field(value = "content")
    private String content;
    @Field(value = "media_reference")
    private String mediaReference;
    @Field(value = "story_type")
    private StoryTypeEnum storyType;
    @Builder.Default
    @Field(value = "is_deleted", targetType = FieldType.BOOLEAN)
    private boolean isDeleted = false;
}
