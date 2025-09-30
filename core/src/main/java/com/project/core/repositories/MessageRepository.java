package com.project.core.repositories;

import com.project.core.domain.models.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MessageRepository extends MongoRepository<Message, Long> {

    @Query(value = "{ 'chatId' : ?0 }", sort = "{ 'createdAt' : -1 }")
    List<Message> findMessagesByChatId(String chatId, Pageable pageable);

    @Query(value = "{ 'chatId' : ?0, 'createdAt' : { '$gt' : ?1 } }", count = true)
    long findUnreadMessageCount(String chatId, LocalDateTime createdAt);

}

