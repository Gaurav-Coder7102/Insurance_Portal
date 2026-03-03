package com.insuranceportal.repository;

import com.insuranceportal.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepository — Data Access Layer for User entity.
 *
 * Spring Data JPA automatically provides:
 *   - save(), findById(), findAll(), delete(), count(), etc.
 *
 * Custom queries defined below are auto-implemented by Spring Data
 * using method name conventions (no SQL needed).
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find a user by their email address.
     * Used during login authentication.
     *
     * @param email the email to search for
     * @return Optional<User> — empty if not found
     */
    Optional<User> findByEmail(String email);

    /**
     * Find a user by their username.
     * Used by UserDetailsService for Spring Security.
     *
     * @param username the username to search for
     * @return Optional<User> — empty if not found
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a user with the given email already exists.
     * Used during registration to prevent duplicates.
     *
     * @param email the email to check
     * @return true if email is already taken
     */
    boolean existsByEmail(String email);

    /**
     * Check if a user with the given username already exists.
     * Used during registration to prevent duplicates.
     *
     * @param username the username to check
     * @return true if username is already taken
     */
    boolean existsByUsername(String username);
}
