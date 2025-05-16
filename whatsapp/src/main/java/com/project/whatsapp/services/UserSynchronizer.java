package com.project.whatsapp.services;

import org.springframework.security.oauth2.jwt.Jwt;

public interface UserSynchronizer {
    void synchronizeWithIdp(Jwt token);
}
