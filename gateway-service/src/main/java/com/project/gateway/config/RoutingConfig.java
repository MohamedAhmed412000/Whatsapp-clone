package com.project.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoutingConfig {

    @Bean
    RouteLocator generateRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("core", p -> p
                .path("/core/api/v1/users/**", "/core/api/v1/messages/**", "/core/api/v1/chats/**",
                    "/core/**")
                .filters(f -> f
                    .rewritePath("/core/(?<api>/?.*)", "/${api}")
                )
                .uri("lb://CORE")
            )
            .route("core-ws", p -> p
                .path("/ws/**")
                .filters(f -> f
                    .rewritePath("/ws(?<segment>.*)", "/ws${segment}")
                )
                .uri("lb://CORE")
            )
            .route("media", p -> p
                .path("/api/v1/media/**")
                .filters(f -> f
                    .rewritePath("/(?<api>.*)", "/${api}")
                )
                .uri("lb://MEDIA-MANAGEMENT")
            )
            .build();
    }

}
