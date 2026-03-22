package com.insuranceportal.repository;

import com.insuranceportal.model.RefreshToken;
import com.insuranceportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * RefreshTokenRepository — Data access for RefreshToken entities.
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * Find a refresh token by its token string.
     * Used when the client sends a refresh request.
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Delete all refresh tokens belonging to a user.
     * Called on logout to invalidate all sessions for the user.
     *
     * @Modifying required because it's a DELETE query.
     */
    @Modifying
    int deleteByUser(User user);
}
