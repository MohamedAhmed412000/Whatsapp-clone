package com.project.gateway.config;

import com.project.gateway.constants.Headers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .cors(ServerHttpSecurity.CorsSpec::disable)
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchange -> exchange
                .pathMatchers(getSwaggerPaths()).permitAll()
                .pathMatchers("/ws/**").permitAll()
                .pathMatchers("/configuration/ui", "/configuration/security").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec.jwt(jwt -> jwt.jwtAuthenticationConverter(
                    source -> Mono.just(Objects.requireNonNull(
                        new KeycloakJwtAuthenticationConverter().convert(source)))
                )));

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("http://localhost:4200"));
        config.setAllowedHeaders(Arrays.asList(
            HttpHeaders.ORIGIN,
            HttpHeaders.CONTENT_TYPE,
            HttpHeaders.ACCEPT,
            HttpHeaders.AUTHORIZATION,
            Headers.REQUEST_ID
        ));
        config.setAllowedMethods(Arrays.asList(HttpMethod.GET.name(), HttpMethod.POST.name(),
            HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.PATCH.name()));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private String[] getSwaggerPaths() {
        return List.of("/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").toArray(String[]::new);
    }

}
