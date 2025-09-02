package com.project.story.repositories;

import com.project.story.domain.models.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoryRepository extends MongoRepository<Story, Long> {
    @Query(value = "{ 'user_id': ?0, 'is_deleted': false }", sort = "{ 'created_at': -1 }")
    List<Story> findStoryByUserId(String userId);
}
