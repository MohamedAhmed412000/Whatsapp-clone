package com.project.whatsapp.domain.models;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;

import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public abstract class BaseModel {
    @CreatedDate
    @Field(value = "created_at")
    private LocalDateTime createdAt;

    @CreatedBy
    @Size(max = 20)
    @Field(value = "created_by")
    private String createdBy;

    @LastModifiedDate
    @Field(value = "updated_at")
    private LocalDateTime updatedAt;

    @LastModifiedBy
    @Size(max = 20)
    @Field(value = "updated_by")
    private String updatedBy;
}