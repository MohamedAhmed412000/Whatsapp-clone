package com.project.whatsapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.project.whatsapp.constants.Application.LAST_ACTIVE_INTERVAL_IN_MINUTES;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "user")
public class User extends BaseModel {
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

    public boolean isOnlineUser() {
        // last seen => 10:05
        // now (10:09) => active
        // now (10:12) => offline
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(
            LAST_ACTIVE_INTERVAL_IN_MINUTES));
    }
    
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return countryCode + phoneNumber;
    }
}
