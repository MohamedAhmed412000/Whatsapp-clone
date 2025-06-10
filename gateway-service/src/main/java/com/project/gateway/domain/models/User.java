package com.project.gateway.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "\"user\"")
public class User {
    @Id
    @Column(value = "ID")
    private UUID id;
    @Column(value = "FIRST_NAME")
    private String firstName;
    @Column(value = "LAST_NAME")
    private String lastName;
    @Column(value = "EMAIL")
    private String email;
    @Column(value = "COUNTRY_CODE")
    private String countryCode;
    @Column(value = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(value = "LAST_SEEN")
    private LocalDateTime lastSeen;
}
