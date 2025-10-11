package com.project.gateway.config;

import com.project.gateway.constants.Headers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Value("${frontend.url}")
    private String frontendUrl;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers(getSwaggerPaths()).permitAll()
                .pathMatchers("/core/actuator/**", "/media/actuator/**").permitAll()
                .pathMatchers("/ws/**", "/ws").permitAll()
                .pathMatchers("/configuration/ui", "/configuration/security").permitAll()
                .anyExchange().permitAll()
            )
            .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec.jwt(jwt -> jwt.jwtAuthenticationConverter(
                    source -> Mono.just(Objects.requireNonNull(
                        new KeycloakJwtAuthenticationConverter().convert(source)))
                )));

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(frontendUrl));
        config.setAllowedMethods(Arrays.asList(
            HttpMethod.GET.name(),
            HttpMethod.POST.name(),
            HttpMethod.PUT.name(),
            HttpMethod.DELETE.name(),
            HttpMethod.PATCH.name(),
            HttpMethod.HEAD.name()
        ));
        config.setAllowedHeaders(Arrays.asList(
            HttpHeaders.ORIGIN,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT,
            HttpHeaders.AUTHORIZATION,
            Headers.REQUEST_ID,
            Headers.USER_ID,
            Headers.USER_EMAIL,
            Headers.ACCEPT_WEB_SOCKETS,
            HttpHeaders.UPGRADE,
            HttpHeaders.CONNECTION
        ));
        config.setExposedHeaders(List.of(HttpHeaders.AUTHORIZATION));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private String[] getSwaggerPaths() {
        return List.of("/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").toArray(String[]::new);
    }
}
