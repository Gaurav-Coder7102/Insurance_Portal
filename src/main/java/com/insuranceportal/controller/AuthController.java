package com.insuranceportal.controller;

import com.insuranceportal.dto.AuthResponse;
import com.insuranceportal.dto.LoginRequest;
import com.insuranceportal.dto.RegisterRequest;
import com.insuranceportal.model.RefreshToken;
import com.insuranceportal.model.User;
import com.insuranceportal.repository.UserRepository;
import com.insuranceportal.security.JwtUtil;
import com.insuranceportal.service.AuthService;
import com.insuranceportal.service.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController — REST endpoints for authentication.
 *
 * Base URL: /api/auth
 *
 * Endpoints:
 * POST /api/auth/register  → Register a new user (public)
 * POST /api/auth/login     → Login, returns JWT + refresh token (public)
 * GET  /api/auth/me        → Get current user info (protected)
 * POST /api/auth/refresh   → Get new access token from refresh token (public)
 * POST /api/auth/logout    → Revoke all refresh tokens (protected)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService         authService;
    private final UserRepository      userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil             jwtUtil;
    private final UserDetailsService  userDetailsService;

    // ===================================================
    // POST /api/auth/register
    // ===================================================

    /**
     * Register a new user.
     *
     * Request Body:
     * { "username": "gaurav", "email": "gaurav@insurance.com", "password":
     * "pass123" }
     *
     * Response (201 Created):
     * { "token": "eyJ...", "type": "Bearer", "id": 1, "username": "gaurav", ... }
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            AuthResponse response = authService.register(request);
            log.info("Registration successful for: {}", request.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            // Email or username already taken
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "error", "Registration failed",
                            "message", e.getMessage()));
        }
    }

    // ===================================================
    // POST /api/auth/login
    // ===================================================

    /**
     * Login with email and password.
     *
     * Request Body:
     * { "email": "gaurav@insurance.com", "password": "pass123" }
     *
     * Response (200 OK):
     * { "token": "eyJ...", "type": "Bearer", "id": 1, "username": "gaurav", ... }
     *
     * Response (401 Unauthorized) — if credentials are wrong:
     * { "error": "Login failed", "message": "Bad credentials" }
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.login(request);
            log.info("Login successful for: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Bad credentials or user not found
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of(
                            "error", "Login failed",
                            "message", "Invalid email or password"));
        }
    }

    // ===================================================
    // GET /api/auth/me (Protected — JWT required)
    // ===================================================

    /**
     * Get the currently authenticated user's profile.
     *
     * Requires: Authorization: Bearer <token> header
     *
     * Response (200 OK):
     * { "id": 1, "username": "gaurav", "email": "gaurav@insurance.com", "role":
     * "ROLE_USER" }
     *
     * Response (401 Unauthorized) — if no/invalid token:
     * Spring Security handles this automatically.
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        // Fetch fresh user data from DB using the email in the JWT
        return userRepository.findByEmail(userDetails.getUsername())
                .map(user -> ResponseEntity.ok(Map.of(
                        "id",       user.getId(),
                        "username", user.getUsername(),
                        "email",    user.getEmail(),
                        "role",     user.getRole(),
                        "enabled",  user.isEnabled())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // ===================================================
    // POST /api/auth/refresh  (Public)
    // ===================================================

    /**
     * Issue a fresh JWT access token using a valid refresh token.
     *
     * Request Body:
     *   { "refreshToken": "550e8400-e29b-41d4-a716-446655440000" }
     *
     * Response (200 OK):
     *   { "token": "eyJ...", "refreshToken": "550e8...", "expiresIn": 86400000, ... }
     *
     * Response (403 Forbidden) — if token is invalid or expired:
     *   { "error": "Invalid refresh token" }
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @RequestBody Map<String, String> request) {

        String requestToken = request.get("refreshToken");
        if (requestToken == null || requestToken.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "refreshToken field is required"));
        }

        try {
            // Find & verify the refresh token
            RefreshToken refreshToken = refreshTokenService.findByToken(requestToken)
                    .orElseThrow(() -> new RuntimeException("Refresh token not found"));

            refreshTokenService.verifyExpiration(refreshToken); // throws if expired

            // Generate new access token
            User user = refreshToken.getUser();
            UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
            String newAccessToken = jwtUtil.generateToken(userDetails);

            log.info("Token refreshed for user: {}", user.getEmail());

            return ResponseEntity.ok(Map.of(
                    "token",        newAccessToken,
                    "refreshToken", refreshToken.getToken(),
                    "type",         "Bearer"
            ));

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of(
                            "error",   "Invalid or expired refresh token",
                            "message", e.getMessage()
                    ));
        }
    }

    // ===================================================
    // POST /api/auth/logout  (Protected — JWT required)
    // ===================================================

    /**
     * Logout — revoke all refresh tokens for the current user.
     *
     * Requires: Authorization: Bearer <token> header
     *
     * Response (200 OK):
     *   { "message": "Logged out successfully" }
     */
    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @AuthenticationPrincipal UserDetails userDetails) {

        userRepository.findByEmail(userDetails.getUsername())
                .ifPresent(refreshTokenService::deleteByUser);

        log.info("User logged out: {}", userDetails.getUsername());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }
}
