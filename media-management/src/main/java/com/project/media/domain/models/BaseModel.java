package com.project.media.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModel {
    @CreatedDate
    @Field("created_at")
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @CreatedBy
    @Size(max = 20)
    @Field("created_by")
    @Builder.Default
    private String createdBy = "SYSTEM";

    @LastModifiedDate
    @Field("updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Size(max = 20)
    @Field("updated_by")
    private String updatedBy;
}