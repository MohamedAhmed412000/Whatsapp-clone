package com.project.whatsapp.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import org.json.JSONObject;

import java.util.Base64;

@Component
public class JwtTokenUtil {

    public String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            return "";
        }
        return bearerToken.substring(7);
    }

    public JSONObject extractTokenPayload(String token) {
        if (token.isEmpty()) return null;
        String[] parts = token.split("\\.");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid JWT token");
        String payloadEncoded = parts[1];
        String payloadDecoded = new String(Base64.getUrlDecoder().decode(payloadEncoded));
        return new JSONObject(payloadDecoded);
    }

    public String getFullnameFromToken(JSONObject tokenClaims) {
        if (tokenClaims == null) return "SYSTEM";
        String firstName = "", lastName = "";
        if (tokenClaims.has("given_name")) {
            firstName = tokenClaims.get("given_name").toString();
        } else if (tokenClaims.has("nickname")) {
            firstName = tokenClaims.get("nickname").toString();
        }

        if (tokenClaims.has("family_name")) {
            lastName = tokenClaims.get("family_name").toString();
        }
        return firstName + " " + lastName;
    }

    public String getUserIdFromToken(JSONObject tokenClaims) {
        if (tokenClaims == null) return "";
        return tokenClaims.get("sub").toString();
    }
}