package com.project.ws.filters;

import com.project.commons.utils.JwtTokenUtil;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChannelInterceptor implements ChannelInterceptor {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        final StompHeaderAccessor accessor = readHeaderAccessor(message);

        if (Objects.equals(accessor.getCommand(), StompCommand.CONNECT)) {
//            String jwtToken = readAuthTokenHeader(accessor);
//
//            JSONObject tokenClaims = jwtTokenUtil.extractTokenPayload(jwtToken);
//            String fullName = jwtTokenUtil.getFullnameFromToken(tokenClaims);
//            String userId = jwtTokenUtil.getUserIdFromToken(tokenClaims);
//            CustomAuthentication customAuthentication = new CustomAuthentication(userId, fullName);
//            accessor.setUser(customAuthentication);
            String userId = accessor.getFirstNativeHeader("user-id"); // header from JS client
            if (userId != null) {
                log.info("Set websocket user id to {}", userId);
                accessor.setUser(new UsernamePasswordAuthenticationToken(userId, null,
                    Collections.emptyList()));
            }
            log.info("WebSocket CONNECT user: {}", Objects.requireNonNull(accessor.getUser()).getName());
            accessor.setHeader("connection-time", LocalDateTime.now().toString());

//            log.info("User with authz '{}', make a websocket connection and generated the user {}",
//                jwtToken, customAuthentication);
        }

        return message;
    }

    private String readAuthTokenHeader(StompHeaderAccessor accessor) {
        String authzHeader = accessor.getFirstNativeHeader("Authorization");
        if (authzHeader == null || authzHeader.trim().isEmpty() || !authzHeader.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Authorization header not found");
        }
        return authzHeader.substring(7);
    }

    private StompHeaderAccessor readHeaderAccessor(Message<?> message) {
        final StompHeaderAccessor accessor = getAccessor(message);
        if (accessor == null) {
            throw new AuthenticationCredentialsNotFoundException("Failed to read headers from message");
        }
        return accessor;
    }

    private StompHeaderAccessor getAccessor(Message<?> message) {
        return MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
    }
}
