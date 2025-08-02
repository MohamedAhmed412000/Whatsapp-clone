package com.project.media.repositories;

import com.project.media.domain.models.Media;
import com.project.media.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomMediaRepository {

    @Value("${application.file.uploads.media-output-path}")
    private String mediaBasePath;
    @Value("${application.file.uploads.media-soft-delete}")
    private boolean mediaSoftDelete;
    private final ReactiveMongoTemplate mongoTemplate;
    private final MediaRepository mediaRepository;

    public Mono<Boolean> deleteMediaById(String reference) {
        if (mediaSoftDelete) {
            return softDeleteMediaById(reference)
                .thenReturn(true)
                .onErrorResume(e -> {
                    log.error("Soft delete failed for reference: {}", reference, e);
                    return Mono.just(false);
                });
        } else {
            return mediaRepository.findMediaById(reference)
                .flatMap(media ->
                    Mono.fromCallable(() -> FileUtils.deleteLocalFile(
                        mediaBasePath, media.getReference(), media.getName()))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(isDeleted -> {
                            if (isDeleted) {
                                return mediaRepository.delete(media).thenReturn(true);
                            } else {
                                return Mono.just(false);
                            }
                        })
                )
                .switchIfEmpty(Mono.just(false)) // Media not found
                .onErrorResume(e -> {
                    log.error("Hard delete failed for reference: {}", reference, e);
                    return Mono.just(false);
                });
        }
    }

    private Mono<Void> softDeleteMediaById(String reference) {
        Query query = new Query(Criteria.where("_id").is(reference));
        Update update = new Update().set("is_deleted", true);
        return mongoTemplate.updateFirst(query, update, Media.class).then();
    }
}
