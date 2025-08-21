package com.project.whatsapp.services.impl;

import com.project.whatsapp.domain.models.Message;
import com.project.whatsapp.domain.models.StarredMessage;
import com.project.whatsapp.mappers.MessageMapper;
import com.project.whatsapp.repositories.StarredMessageRepository;
import com.project.whatsapp.rest.outbound.StarredMessageResponse;
import com.project.whatsapp.services.StarredMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.project.whatsapp.constants.Application.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class StarredMessageServiceImpl implements StarredMessageService {

    private final MongoTemplate mongoTemplate;
    private final StarredMessageRepository starredMessageRepository;
    private final MessageMapper messageMapper;

    @Override
    public List<StarredMessageResponse> findStarredMessages(int page) {
        String userId = getUserId();
        return findStarredMessagesByUserId(userId, page).stream().map(
            messageMapper::toStarredMessageResponse).toList();
    }

    @Override
    public boolean starMessage(Long messageId) {
        String userId = getUserId();
        Optional<StarredMessage> starredMessageOptional = starredMessageRepository.
            findStarredMessageByUserIdAndMessageId(userId, messageId);
        if (starredMessageOptional.isPresent()) return false;
        StarredMessage starredMessage = StarredMessage.builder()
            .userId(userId)
            .messageId(messageId)
            .build();
        starredMessageRepository.save(starredMessage);
        return true;
    }

    @Override
    public boolean unstarMessage(Long messageId) {
        String userId = getUserId();
        Optional<StarredMessage> starredMessageOptional = starredMessageRepository.
            findStarredMessageByUserIdAndMessageId(userId, messageId);
        if (starredMessageOptional.isPresent()) {
            starredMessageRepository.delete(starredMessageOptional.get());
            return true;
        }
        return false;
    }

    private List<Message> findStarredMessagesByUserId(String userId, int page) {
        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(Criteria.where("user_id").is(userId)),
            Aggregation.lookup("message", "message_id", "_id", "messageInfo"),
            Aggregation.unwind("messageInfo"),
            Aggregation.replaceRoot("messageInfo"),
            Aggregation.skip((long) page * PAGE_SIZE),
            Aggregation.limit(PAGE_SIZE)
        );

        AggregationResults<Message> results = mongoTemplate.aggregate(
            aggregation, "starred_message", Message.class
        );

        return results.getMappedResults();
    }

    private String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }
}
