package com.project.media.repositories;

import com.project.media.domain.models.Media;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MediaRepository extends ReactiveMongoRepository<Media, String> {
    @Query(value = "{ 'entity_id' : ?0, 'is_deleted' : false }")
    Flux<Media> findMediaByEntityId(String entityId);

    @Query(value = "{ '_id' : ?0, 'is_deleted' : false }")
    Mono<Media> findMediaById(String id);
}
