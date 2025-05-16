package com.project.whatsapp.domain.models;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseModel {
    @CreatedDate
    @Column(name = "CREATED_AT", updatable = false)
    private LocalDateTime createdAt;
    @CreatedBy
    @Size(max = 20)
    @Column(name = "CREATED_BY", updatable = false)
    private String createdBy;
    @LastModifiedDate
    @Column(name = "UPDATED_AT", insertable = false)
    private LocalDateTime updatedAt;
    @LastModifiedBy
    @Size(max = 20)
    @Column(name = "UPDATED_BY", insertable = false)
    private String updatedBy;
}
