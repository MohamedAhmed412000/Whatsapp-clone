package com.project.whatsapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.project.whatsapp.constants.Application.LAST_ACTIVE_INTERVAL_IN_MINUTES;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@Table("USER")
public class User extends BaseModel {
    @Id
    private UUID id;
    @Column("FIRST_NAME")
    private String firstName;
    @Column("LAST_NAME")
    private String lastName;
    @Column("COUNTRY_CODE")
    private String countryCode;
    @Column("PHONE_NUMBER")
    private String phoneNumber;
    @Column("EMAIL")
    private String email;
    @Column("LAST_SEEN")
    private LocalDateTime lastSeen;
    @Column("PROFILE_PICTURE_URL")
    private String profilePictureUrl;

    @Transient
    public boolean isOnlineUser() {
        // last seen => 10:05
        // now (10:09) => active
        // now (10:12) => offline
        return lastSeen != null && lastSeen.isAfter(LocalDateTime.now().minusMinutes(
            LAST_ACTIVE_INTERVAL_IN_MINUTES));
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public User() {
        this.id = UUID.randomUUID();
    }
}

