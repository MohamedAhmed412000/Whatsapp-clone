package com.project.gateway.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User {
    @MongoId
    @Field(value = "_id")
    private UUID id = UUID.randomUUID();
    @Field(value = "first_name")
    private String firstName;
    @Field(value = "last_name")
    private String lastName;
    @Field(value = "email")
    private String email;
    @Field(value = "country_code")
    private String countryCode;
    @Field(value = "phone_number")
    private String phoneNumber;
    @Field(value = "last_seen", targetType = FieldType.TIMESTAMP)
    private LocalDateTime lastSeen;
    @Field(value = "profile_picture_url")
    private String profilePictureUrl;
}
