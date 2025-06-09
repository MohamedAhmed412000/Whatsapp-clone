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
            .route(p -> p
                .path("/api/v1/users/**", "/api/v1/messages/**", "/api/v1/chats/**")
                .filters(f -> f
                    .rewritePath("/(?<api>.*)", "/${api}")
                )
                .uri("lb://CORE")
            )
            .route(p -> p
                .path("/api/v1/media/**")
                .filters(f -> f
                    .rewritePath("/(?<api>.*)", "/${api}")
                )
                .uri("lb://MEDIA-MANAGEMENT")
            )
            .build();
    }

}
