package com.insuranceportal.service;

import com.insuranceportal.model.RefreshToken;
import com.insuranceportal.model.User;
import com.insuranceportal.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * RefreshTokenService — Manages the lifecycle of refresh tokens.
 *
 * Responsibilities:
 *   - createRefreshToken()  : Generate, persist, and return a new refresh token
 *   - verifyExpiration()    : Check if a token is still valid; throw if not
 *   - deleteByUser()        : Revoke all tokens for a user (logout)
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    @Value("${app.jwt.refresh-expiration-ms}")
    private long refreshExpirationMs;

    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Create and persist a new refresh token for the given user.
     *
     * The token value is a random UUID — simple, unique, and unguessable.
     * Expiry is set to now + refresh-expiration-ms (default 7 days).
     *
     * @param user the authenticated user
     * @return the saved RefreshToken entity
     */
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .build();

        RefreshToken saved = refreshTokenRepository.save(refreshToken);
        log.debug("Created refresh token for user: {}", user.getEmail());
        return saved;
    }

    /**
     * Verify the refresh token is not expired.
     *
     * If expired, deleted from DB and throws RuntimeException
     * so the client knows to log in again.
     *
     * @param token the RefreshToken entity to check
     * @return the same token if still valid
     * @throws RuntimeException if the token has expired
     */
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            log.warn("Refresh token expired for user: {}", token.getUser().getEmail());
            throw new RuntimeException(
                "Refresh token has expired. Please log in again."
            );
        }
        return token;
    }

    /**
     * Find a RefreshToken by its token string.
     *
     * @param token the raw token string sent by the client
     * @return an Optional containing the token if found
     */
    public java.util.Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Delete all refresh tokens for a user.
     * Call this on logout to invalidate all their sessions.
     *
     * @param user the user whose tokens should be revoked
     */
    @Transactional
    public void deleteByUser(User user) {
        int deleted = refreshTokenRepository.deleteByUser(user);
        log.info("Revoked {} refresh token(s) for user: {}", deleted, user.getEmail());
    }
}
