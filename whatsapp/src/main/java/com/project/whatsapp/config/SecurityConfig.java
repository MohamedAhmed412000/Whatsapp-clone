package com.project.whatsapp.config;

import com.project.whatsapp.constants.Headers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .cors(withDefaults())
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(authorizeExchangeSpec -> {
                    authorizeExchangeSpec.pathMatchers(getSwaggerPaths()).permitAll();
                    authorizeExchangeSpec.pathMatchers("/ws/**").permitAll();
                    authorizeExchangeSpec.pathMatchers(
                        "/configuration/ui", "/configuration/security"
                    ).permitAll();
                    authorizeExchangeSpec.anyExchange().authenticated();
                }
            )
            .oauth2ResourceServer(oAuth2ResourceServerSpec ->
                oAuth2ResourceServerSpec.jwt(jwt -> jwt.jwtAuthenticationConverter(
                    new KeycloakJwtAuthenticationConverter()
                )));
        return http.build();
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
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
        return new CorsWebFilter(source);
    }

    private String[] getSwaggerPaths() {
        return List.of("/v3/api-docs", "/v3/api-docs/**", "/swagger-resources", "/swagger-resources/**",
            "/swagger-ui/**", "/webjars/**", "/swagger-ui.html").toArray(String[]::new);
    }

}
