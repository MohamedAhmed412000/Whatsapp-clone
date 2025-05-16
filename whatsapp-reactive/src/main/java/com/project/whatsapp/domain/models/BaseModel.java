package com.project.whatsapp.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class BaseModel {
    @Column("CREATED_AT")
    private LocalDateTime createdAt;
    @Column("UPDATED_AT")
    private LocalDateTime updatedAt;
}
