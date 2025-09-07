package com.project.user.utils;

import com.project.user.exceptions.UserNotFoundException;
import lombok.Data;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
public class KeycloakUtil {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakUtil(
        @Value("${keycloak.server-url}") String serverUrl,
        @Value("${keycloak.client-id}") String clientId,
        @Value("${keycloak.client-secret}") String clientSecret,
        @Value("${keycloak.realm}") String realm
    ) {
        this.keycloak = KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    }

    public void updateUserProfile(
        String userId, String firstName, String lastName, String pictureUrlReference
    ) {
        UserResource userResource = keycloak.realm(realm).users().get(userId);
        if (userResource == null) {
            throw new UserNotFoundException("User not found");
        }

        UserRepresentation user = userResource.toRepresentation();
        if (firstName != null && !firstName.isEmpty()) user.setFirstName(firstName);
        if (lastName != null && !lastName.isEmpty()) user.setLastName(lastName);
        if (pictureUrlReference != null) {
            Map<String, List<String>> attributes = new HashMap<>();
            attributes.put("imageUrl", Collections.singletonList(pictureUrlReference));
            user.setAttributes(attributes);
        }
        userResource.update(user);
    }

}
