package com.insuranceportal.security;

import com.insuranceportal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * UserDetailsServiceImpl — Loads user details from PostgreSQL for Spring Security.
 *
 * Spring Security calls this during authentication to fetch the user
 * by their identifier (we use email as the username).
 *
 * The returned UserDetails object is used to:
 *   - Verify the password (during login)
 *   - Set authorities/roles
 *   - Populate the SecurityContext
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load a user by their email address.
     *
     * NOTE: Spring Security calls this with the "username" field,
     * but we've configured email as the login identifier.
     *
     * @param email the email address used as login identifier
     * @return UserDetails — Spring Security representation of the user
     * @throws UsernameNotFoundException if no user with the given email exists
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getEmail(),             // username/principal
                        user.getPassword(),           // BCrypt hashed password
                        user.isEnabled(),             // account enabled flag
                        true,                         // account non-expired
                        true,                         // credentials non-expired
                        true,                         // account non-locked
                        List.of(new SimpleGrantedAuthority(user.getRole())) // authorities
                ))
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );
    }
}
