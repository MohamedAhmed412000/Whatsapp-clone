package com.project.gateway.mappers;

import com.project.gateway.domain.models.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Component
public class UserMapper {

    public User fromTokenClaims(Map<String, Object> claims) {
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

        if (claims.containsKey("email")) {
            user.setEmail(claims.get("email").toString());
        }

        if (claims.containsKey("country_code") && claims.containsKey("phone_number")) {
            user.setCountryCode(claims.get("country_code").toString());
            user.setPhoneNumber(claims.get("phone_number").toString());
        }

        user.setLastSeen(LocalDateTime.now());
        return user;
    }

}
