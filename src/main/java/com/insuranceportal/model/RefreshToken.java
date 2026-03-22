package com.insuranceportal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * RefreshToken — Persisted refresh token entity.
 *
 * Each record links a long-lived refresh token string to a User.
 * When a client's access token expires, they can call POST /api/auth/refresh
 * with their refreshToken to get a new access token without logging in again.
 *
 * Stored in the "refresh_tokens" table in PostgreSQL.
 */
@Entity
@Table(name = "refresh_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The refresh token value (UUID string).
     * Unique per record — also indexed for fast lookup.
     */
    @Column(nullable = false, unique = true)
    private String token;

    /**
     * The user this refresh token belongs to.
     * One user can have multiple active refresh tokens
     * (e.g., logged in from multiple devices).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Expiry timestamp — tokens past this instant are invalid.
     * Default: 7 days from creation (configured via app.jwt.refresh-expiration-ms).
     */
    @Column(nullable = false)
    private Instant expiryDate;
}
