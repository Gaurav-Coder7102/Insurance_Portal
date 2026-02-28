package com.insuranceportal.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtAuthFilter — Intercepts every HTTP request to validate the JWT token.
 *
 * Flow:
 *   1. Extract "Authorization: Bearer <token>" header
 *   2. Validate the token using JwtUtil
 *   3. Load user from DB (via UserDetailsService)
 *   4. If valid, set authentication in SecurityContextHolder
 *   5. Pass the request along the filter chain
 *
 * Extends OncePerRequestFilter → guaranteed to run exactly once per request.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1: Read the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // If no Bearer token → skip filter, let Spring Security handle it
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 2: Extract the JWT token (remove "Bearer " prefix)
        final String jwt = authHeader.substring(7);
        String userEmail;

        try {
            userEmail = jwtUtil.extractUsername(jwt);
        } catch (Exception e) {
            log.warn("Failed to extract username from JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: If username extracted and no existing auth in context → authenticate
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Step 4: Validate the token
            if (jwtUtil.isTokenValid(jwt, userDetails)) {

                // Create authentication token and set in SecurityContext
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.debug("Authenticated user: {}", userEmail);
            }
        }

        // Step 5: Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
