package com.project.user.services.impl;

import com.project.user.domain.models.Contact;
import com.project.user.domain.models.User;
import com.project.user.exceptions.ContactAlreadyExistsException;
import com.project.user.exceptions.ContactNotFoundException;
import com.project.user.exceptions.UpdateActionNotAllowedException;
import com.project.user.exceptions.UserNotFoundException;
import com.project.user.mappers.UserMapper;
import com.project.user.repositories.UserContactRepository;
import com.project.user.repositories.UserRepository;
import com.project.user.rest.inbound.UserContactCreationResource;
import com.project.user.rest.inbound.UserContactUpdateResource;
import com.project.user.rest.outbound.UserResponse;
import com.project.user.services.UserContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserContactServiceImpl implements UserContactService {

    private final MongoTemplate mongoTemplate;
    private final UserContactRepository userContactRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Long addNewContact(UserContactCreationResource resource) {
        User user = userRepository.findByEmail(resource.getEmail())
            .orElseThrow(UserNotFoundException::new);
        String ownerId = getUserId();

        userContactRepository.findContactByOwnerIdAndUserId(ownerId, user.getId()).ifPresent(contact -> {
            throw new ContactAlreadyExistsException("Contact already exists");
        });

        Contact contact = Contact.builder()
            .id(System.currentTimeMillis())
            .ownerId(ownerId)
            .userId(user.getId())
            .fullName(resource.getFullName() != null ? resource.getFullName() : user.getFullName())
            .isFavourite(Boolean.TRUE.equals(resource.getIsFavourite()))
            .build();

        return userContactRepository.save(contact).getId();
    }

    @Override
    public List<UserResponse> getUserContacts(String query) {
        return getContactUsers(getUserId(), query, false).stream()
            .map(user -> userMapper.toUserResponse(user, false, null))
            .toList();
    }

    @Override
    public List<UserResponse> getUserBlockedContacts() {
        return getContactUsers(getUserId(), null, true).stream()
            .map(user -> userMapper.toUserResponse(user, false, null))
            .toList();
    }

    @Override
    public boolean updateContact(String userId, UserContactUpdateResource resource) {
        try {
            Contact contact = getContact(userId);
            contact.setFullName(resource.getFullName());
            userContactRepository.save(contact);
            return true;
        } catch (ContactNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean pinContact(String userId) {
        try {
            Contact contact = getContact(userId);
            if (contact.isBlocked())
                throw new UpdateActionNotAllowedException("Contact is blocked");
            contact.setFavourite(Boolean.TRUE);
            userContactRepository.save(contact);
            return true;
        } catch (ContactNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean unpinContact(String userId) {
        try {
            Contact contact = getContact(userId);
            if (contact.isBlocked())
                throw new UpdateActionNotAllowedException("Contact is blocked");
            contact.setFavourite(Boolean.FALSE);
            userContactRepository.save(contact);
            return true;
        } catch (ContactNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean blockContact(String userId) {
        try {
            Contact contact = getContact(userId);
            if (contact.isBlocked())
                throw new UpdateActionNotAllowedException("Contact is already blocked");
            contact.setBlocked(Boolean.TRUE);
            userContactRepository.save(contact);
            return true;
        } catch (ContactNotFoundException e) {
            User user = userRepository.findByPublicId(userId)
                .orElseThrow(UserNotFoundException::new);
            Contact contact = Contact.builder()
                .id(System.currentTimeMillis())
                .ownerId(getUserId())
                .userId(userId)
                .fullName(user.getFullName())
                .isBlocked(Boolean.TRUE)
                .build();

            userContactRepository.save(contact);
            return true;
        }
    }

    @Override
    public boolean unblockContact(String userId) {
        try {
            Contact contact = getContact(userId);
            if (!contact.isBlocked())
                throw new UpdateActionNotAllowedException("Contact isn't blocked");
            contact.setBlocked(Boolean.FALSE);
            userContactRepository.save(contact);
            return true;
        } catch (ContactNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean isContactBlocked(String userId) {
        String myUserId = getUserId();
        return userContactRepository.findBlockedContactByUserId(userId, myUserId).isPresent();
    }

    private Contact getContact(String userId) {
        String ownerId = getUserId();
        return userContactRepository.findContactByOwnerIdAndUserId(ownerId, userId)
            .orElseThrow(ContactNotFoundException::new);
    }

    private List<User> getContactUsers(String userId, String query, boolean isBlocked) {
        Criteria criteria = Criteria.where("owner_id").is(userId)
            .and("is_blocked").is(isBlocked);
        if (query != null && !query.isEmpty()) {
            criteria = criteria.and("full_name").regex(query, "i");
        }

        Aggregation aggregation = Aggregation.newAggregation(
            Aggregation.match(criteria),
            Aggregation.sort(Sort.by(Sort.Direction.DESC, "is_favourite").and(
                Sort.by(Sort.Direction.ASC, "full_name")
            )),

            Aggregation.lookup("user", "user_id", "_id", "userInfo"),
            Aggregation.unwind("userInfo"),
            Aggregation.replaceRoot("userInfo")
        );

        AggregationResults<User> results = mongoTemplate.aggregate(
            aggregation, "contact", User.class
        );

        return results.getMappedResults();
    }

    private String getUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal().toString();
    }
}
