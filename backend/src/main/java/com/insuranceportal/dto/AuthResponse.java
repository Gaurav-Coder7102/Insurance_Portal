package com.insuranceportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AuthResponse DTO — Returned by both /api/auth/login and /api/auth/register
 *
 * The "token" field is the JWT that the Next.js frontend must store
 * (localStorage or httpOnly cookie) and send in subsequent requests as:
 *
 *   Authorization: Bearer <token>
 *
 * Example JSON response:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiJ9...",
 *   "type": "Bearer",
 *   "id": 1,
 *   "username": "gaurav",
 *   "email":    "gaurav@insurance.com",
 *   "role":     "ROLE_USER"
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    /** JWT access token */
    private String token;

    /** Token type — always "Bearer" */
    @Builder.Default
    private String type = "Bearer";

    /** User ID */
    private Long id;

    /** Username */
    private String username;

    /** Email address */
    private String email;

    /** Role — "ROLE_USER" or "ROLE_ADMIN" */
    private String role;
}
