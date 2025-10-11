package com.project.media.repositories;

import com.project.media.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.File;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CustomMediaRepository {

    @Value("${application.file.uploads.media-output-path}")
    private String mediaBasePath;
    @Value("${application.file.uploads.media-soft-delete}")
    private boolean mediaSoftDelete;
    private final MediaRepository mediaRepository;

    public Mono<Boolean> deleteMediaById(String reference) {
        if (mediaSoftDelete) {
            return mediaRepository.findMediaById(reference)
                .flatMap(media -> {
                    media.setIsDeleted(true);
                    return mediaRepository.save(media);
                })
                .thenReturn(true)
                .onErrorResume(e -> {
                    log.error("Soft delete failed for reference: {}", reference, e);
                    return Mono.just(false);
                });
        } else {
            return mediaRepository.findMediaById(reference)
                .flatMap(media ->
                    Mono.fromCallable(() -> FileUtils.deleteLocalFile(
                        mediaBasePath, generateMediaReferencePath(media.getReference()), media.getName()))
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(isDeleted -> {
                            if (isDeleted) {
                                return mediaRepository.delete(media).thenReturn(true);
                            } else {
                                return Mono.just(false);
                            }
                        })
                )
                .switchIfEmpty(Mono.just(false))
                .onErrorResume(e -> {
                    log.error("Hard delete failed for reference: {}", reference, e);
                    return Mono.just(false);
                });
        }
    }

    private String generateMediaReferencePath(String dbReference) {
        return dbReference.replace(",", File.separator);
    }
}
