package com.project.media.repositories;

import com.project.media.domain.models.Media;
import com.project.media.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomMediaRepository {

    @Value("${application.file.uploads.media-output-path}")
    private String mediaBasePath;
    @Value("${application.file.uploads.media-soft-delete}")
    private boolean mediaSoftDelete;
    private final ReactiveMongoTemplate mongoTemplate;

    public void deleteMediaByReference(String reference) {
        if (mediaSoftDelete) {
            softDeleteMediaByReference(reference);
        } else {
            FileUtils.deleteLocalFile(mediaBasePath, reference);
            hardDeleteMediaByReference(reference);
        }
    }

    private void softDeleteMediaByReference(String reference) {
        Query query = new Query(Criteria.where("reference").is(reference));
        Update update = new Update().set("is_deleted", true);
        mongoTemplate.updateFirst(query, update, Media.class).then().block();
    }

    private void hardDeleteMediaByReference(String reference) {
        Query query = new Query(Criteria.where("reference").is(reference));
        mongoTemplate.remove(query, Media.class).then().block();
    }
}
