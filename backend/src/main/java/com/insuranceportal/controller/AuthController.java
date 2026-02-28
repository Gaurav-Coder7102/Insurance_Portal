package com.insuranceportal.controller;

import com.insuranceportal.dto.AuthResponse;
import com.insuranceportal.dto.LoginRequest;
import com.insuranceportal.dto.RegisterRequest;
import com.insuranceportal.model.User;
import com.insuranceportal.repository.UserRepository;
import com.insuranceportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AuthController — REST endpoints for authentication.
 *
 * Base URL: /api/auth
 *
 * Endpoints:
 * POST /api/auth/register → Register a new user (public)
 * POST /api/auth/login → Login, returns JWT (public)
 * GET /api/auth/me → Get current user info (protected, requires JWT)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

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
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "email", user.getEmail(),
                        "role", user.getRole(),
                        "enabled", user.isEnabled())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
