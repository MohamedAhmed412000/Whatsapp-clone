package com.project.media.repositories;

import com.project.media.domain.models.Media;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface MediaRepository extends ReactiveMongoRepository<Media, UUID> {
    @Query(value = "{ 'entity_id' : ?0 }")
    Flux<Media> findMediaByEntityId(String entityId);

    @Query(value = "{ 'reference' : ?0 }")
    Mono<Media> findMediaByReference(String reference);
}
