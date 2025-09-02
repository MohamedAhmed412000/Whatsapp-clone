package com.project.commons.filters;

import com.project.commons.filters.dto.CustomAuthentication;
import com.project.commons.utils.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, @NonNull  HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenUtil.extractToken(request);
        JSONObject tokenClaims = jwtTokenUtil.extractTokenPayload(token);
        String fullName = jwtTokenUtil.getFullnameFromToken(tokenClaims);
        String userId = jwtTokenUtil.getUserIdFromToken(tokenClaims);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(new CustomAuthentication(userId, fullName));
        filterChain.doFilter(request, response);
    }
}
