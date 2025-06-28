package com.project.gateway.repositories;

import com.project.gateway.domain.models.User;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    @Modifying
    @Query("INSERT INTO \"user\" (id, first_name, last_name, email, country_code, phone_number, " +
        "last_seen, profile_picture_url, created_at, created_by) " +
        "VALUES (:#{#user.id}, :#{#user.firstName}, :#{#user.lastName}, :#{#user.email}, " +
        ":#{#user.countryCode}, :#{#user.phoneNumber}, :#{#user.lastSeen}, " +
        ":#{#user.profilePictureUrl}, now(), 'SYSTEM') " +
        "ON CONFLICT (id) DO UPDATE SET " +
        "first_name = COALESCE(EXCLUDED.first_name, \"user\".first_name), " +
        "last_name = COALESCE(EXCLUDED.last_name, \"user\".last_name), " +
        "email = COALESCE(EXCLUDED.email, \"user\".email), " +
        "country_code = COALESCE(EXCLUDED.country_code, \"user\".country_code), " +
        "phone_number = COALESCE(EXCLUDED.phone_number, \"user\".phone_number), " +
        "profile_picture_url = COALESCE(EXCLUDED.profile_picture_url, \"user\".profile_picture_url), " +
        "last_seen = EXCLUDED.last_seen," +
        "updated_at = now(), updated_by = 'SYSTEM'"
    )
    Mono<Void> upsertUser(
        @Param("user") User user
    );
}
