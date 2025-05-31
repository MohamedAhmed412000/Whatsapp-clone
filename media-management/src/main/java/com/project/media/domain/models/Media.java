package com.project.media.domain.models;

import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Builder
@Data
@Entity
@Table(name = "MEDIA")
@AllArgsConstructor
@NoArgsConstructor
public class Media extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", updatable = false)
    private String id;
    @Column(name = "ENTITY_ID")
    private String entityId;
    @Column(name = "NAME")
    private String name;
    @Column(name = "SIZE")
    private Long size;
    @Column(name = "REFERENCE")
    private String reference;
}

