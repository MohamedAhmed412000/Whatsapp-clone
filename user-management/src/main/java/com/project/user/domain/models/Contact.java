package com.project.user.domain.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "contact")
public class Contact extends BaseModel {
    @MongoId
    @Field(value = "_id", targetType = FieldType.INT64)
    private Long id;
    @Field(value = "owner_id")
    private String ownerId;
    @Field(value = "user_id")
    private String userId;
    @Field(value = "first_name")
    private String firstname;
    @Field(value = "last_name")
    private String lastname;
    @Builder.Default
    @Field(value = "is_blocked", targetType = FieldType.BOOLEAN)
    private boolean isBlocked = false;
    @Builder.Default
    @Field(value = "is_favourite", targetType = FieldType.BOOLEAN)
    private boolean isFavourite = false;

    public String getFullName() {
        return firstname + " " + lastname;
    }
}
