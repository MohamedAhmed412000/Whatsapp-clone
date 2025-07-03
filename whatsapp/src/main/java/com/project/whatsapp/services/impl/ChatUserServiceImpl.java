package com.project.whatsapp.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatUserServiceImpl {

    private final MongoTemplate mongoTemplate;

    LocalDateTime findLastMessageViewedFromAllMembers(UUID chatId) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("chatId").is(chatId)),
            Aggregation.sort(Sort.by("lastSeenMessageAt").ascending()),
            Aggregation.limit(1),
            Aggregation.project("lastSeenMessageAt")
        );

        AggregationResults<LocalDateTime> result = mongoTemplate.aggregate(
            aggregation,
            "chat_user",
            LocalDateTime.class
        );

        return result.getUniqueMappedResult();
    }

}
