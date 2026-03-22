package com.insuranceportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthResponse DTO — Returned by both /api/auth/login and /api/auth/register
 *
 * The "token" is a short-lived JWT access token (24h).
 * The "refreshToken" is a long-lived token (7 days) used to get a new access token.
 *
 * Example JSON response:
 * {
 *   "token":        "eyJhbGciOiJIUzI1NiJ9...",
 *   "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
 *   "type":         "Bearer",
 *   "expiresIn":    86400000,
 *   "id":           1,
 *   "username":     "gaurav",
 *   "email":        "gaurav@insurance.com",
 *   "role":         "ROLE_USER"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** JWT access token (short-lived — 24h) */
    private String token;

    /** Refresh token (long-lived — 7 days) used to obtain a new access token */
    private String refreshToken;

    /** Token type — always "Bearer" */
    @Builder.Default
    private String type = "Bearer";

    /** Access token lifetime in milliseconds (matches app.jwt.expiration-ms) */
    private long expiresIn;

    /** User ID */
    private Long id;

    /** Username */
    private String username;

    /** Email address */
    private String email;

    /** Role — "ROLE_USER" or "ROLE_ADMIN" */
    private String role;
}
