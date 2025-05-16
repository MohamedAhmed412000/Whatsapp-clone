package com.project.whatsapp.mappers;

import com.project.whatsapp.domain.models.User;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

public class UserMapper {

    public static User fromTokenClaims(Map<String, Object> claims) {
        User user = new User();
        if(claims.containsKey("sub")) {
            user.setId(UUID.fromString(claims.get("sub").toString()));
        }

        if(claims.containsKey("given_name")) {
            user.setFirstName(claims.get("given_name").toString());
        } else if (claims.containsKey("nickname")) {
            user.setFirstName(claims.get("nickname").toString());
        }

        if(claims.containsKey("family_name")) {
            user.setLastName(claims.get("family_name").toString());
        }

        if(claims.containsKey("email")) {
            user.setEmail(claims.get("email").toString());
        }

        user.setLastSeen(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

}
