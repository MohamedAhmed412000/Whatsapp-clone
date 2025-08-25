package com.project.gateway.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.commons.rest.outbound.RestResponse;
import com.project.commons.rest.outbound.dto.Header;
import com.project.gateway.constants.Application;
import com.project.gateway.constants.Headers;
import lombok.NonNull;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Component
public class FullResponseFilter implements GlobalFilter, Ordered {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if (path.contains("/swagger") || path.contains("/api-docs") || path.contains("/v3/api-docs") ||
            path.contains("/webjars") || path.endsWith(".css") || path.endsWith(".js") ||
            path.endsWith(".html")) {
            return chain.filter(exchange);
        }

        String requestId = UUID.randomUUID().toString();
        ServerHttpResponse originalResponse = exchange.getResponse();

        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
            @Override
            public @NonNull Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux<? extends DataBuffer> fluxBody) {
                    return super.writeWith(
                        fluxBody.buffer().handle((dataBuffers, sink) -> {
                            StringBuilder responseBody = new StringBuilder();
                            dataBuffers.forEach(buffer -> {
                                byte[] bytes = new byte[buffer.readableByteCount()];
                                buffer.read(bytes);
                                DataBufferUtils.release(buffer);
                                responseBody.append(new String(bytes, StandardCharsets.UTF_8));
                            });

                            Object originalJson;
                            String rawBody = responseBody.toString();
                            try {
                                if (rawBody.trim().startsWith("{") || rawBody.trim().startsWith("[")) {
                                    originalJson = objectMapper.readValue(rawBody, Object.class);
                                } else {
                                    originalJson = rawBody;
                                }
                            } catch (JsonProcessingException e) {
                                originalJson = rawBody;
                            }

                            String statusCode = originalResponse.getHeaders().getFirst(Headers.APP_CODE);
                            if (statusCode == null || statusCode.isEmpty())
                                statusCode = Application.SUCCESS_CODE;
                            RestResponse restResponse = RestResponse.builder()
                                .header(Header.builder()
                                    .requestId(requestId)
                                    .applicationCode(statusCode)
                                    .build())
                                .body(originalJson)
                                .build();

                            try {
                                String wrappedJson = objectMapper.writeValueAsString(restResponse);
                                byte[] newResponseBytes = wrappedJson.getBytes(StandardCharsets.UTF_8);

                                getHeaders().remove(Headers.APP_CODE);
                                getHeaders().setContentType(MediaType.APPLICATION_JSON);
                                getHeaders().setContentLength(newResponseBytes.length);

                                sink.next(bufferFactory().wrap(newResponseBytes));
                            } catch (JsonProcessingException e) {
                                sink.error(new RuntimeException("Error wrapping response", e));
                            }
                        })
                    );
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return -2;
    }
}
