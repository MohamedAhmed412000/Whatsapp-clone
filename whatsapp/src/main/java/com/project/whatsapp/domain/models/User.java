package com.project.whatsapp.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.project.whatsapp.constants.Application.LAST_ACTIVE_INTERVAL_IN_MINUTES;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "USER")
public class User extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "FIRST_NAME", nullable = true)
    private String firstName;
    @Column(name = "LAST_NAME", nullable = true)
    private String lastName;
    @Column(name = "COUNTRY_CODE", nullable = false)
    private String countryCode;
    @Column(name = "PHONE_NUMBER", unique = true, nullable = false)
    private String phoneNumber;
    @Column(name = "EMAIL", nullable = true, unique = true)
    private String email;
    @Column(name = "LAST_SEEN")
    private LocalDateTime lastSeen;
    @Column(name = "PROFILE_PICTURE_URL", nullable = true)
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
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        }
        return countryCode + phoneNumber;
    }

}
