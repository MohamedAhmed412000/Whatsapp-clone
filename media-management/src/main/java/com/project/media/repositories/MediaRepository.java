package com.project.media.repositories;

import com.project.media.domain.models.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<Media, UUID> {
    @Query("SELECT m FROM Media m WHERE m.entityId = :entityId")
    List<Media> findMediaByEntityId(String entityId);
}
