package com.project.gateway.config;

import lombok.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter;

    private static final String RESOURCE_ACCESS_CLAIM = "resource_access";
    private static final String ACCOUNT_KEY = "account";
    private static final String ROLES_KEY = "roles";
    private static final String ROLE_PREFIX = "ROLE_";

    public KeycloakJwtAuthenticationConverter() {
        this.jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    }

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt source) {
        Collection<GrantedAuthority> authorities = Stream.concat(
            jwtGrantedAuthoritiesConverter.convert(source).stream(),
            extractResourceRoles(source).stream()
        ).collect(Collectors.toSet());

        return new JwtAuthenticationToken(source, authorities);
    }

    /**
     * Extracts and converts resource roles from a JWT token into Spring Security GrantedAuthorities.
     * Roles are retrieved from the resource_access claim and converted to the format ROLE_XXX.
     *
     * @param jwt The JWT token to extract roles from
     * @return Set of GrantedAuthority representing the user's roles
     */
    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess = jwt.getClaim(RESOURCE_ACCESS_CLAIM);
        if (resourceAccess == null) {
            return Collections.emptySet();
        }

        @SuppressWarnings("unchecked")
        Map<String, List<String>> account = (Map<String, List<String>>) resourceAccess.get(ACCOUNT_KEY);
        if (account == null || !account.containsKey(ROLES_KEY)) {
            return Collections.emptySet();
        }

        return account.get(ROLES_KEY).stream()
            .map(this::formatRole)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toSet());
    }

    private String formatRole(String role) {
        return ROLE_PREFIX + role.replace("-", "_");
    }
}