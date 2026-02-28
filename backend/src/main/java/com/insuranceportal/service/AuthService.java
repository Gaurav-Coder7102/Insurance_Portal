package com.insuranceportal.service;

import com.insuranceportal.dto.AuthResponse;
import com.insuranceportal.dto.LoginRequest;
import com.insuranceportal.dto.RegisterRequest;
import com.insuranceportal.model.User;
import com.insuranceportal.repository.UserRepository;
import com.insuranceportal.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * AuthService — Business logic for user registration and login.
 *
 * Responsibilities:
 * - register() : Validate uniqueness → hash password → save to DB → return JWT
 * - login() : Authenticate credentials → generate JWT → return AuthResponse
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    // ===== Register =====

    /**
     * Register a new user account.
     *
     * Steps:
     * 1. Check email and username are not already taken
     * 2. Hash the password with BCrypt
     * 3. Save user to PostgreSQL
     * 4. Generate and return a JWT token
     *
     * @param request RegisterRequest DTO (username, email, password)
     * @return AuthResponse with JWT token and user info
     * @throws IllegalArgumentException if email or username is already taken
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        // Step 1: Check for duplicate email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException(
                    "Email is already registered: " + request.getEmail());
        }

        // Check for duplicate username
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Username is already taken: " + request.getUsername());
        }

        // Step 2: Build user entity with hashed password
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // BCrypt hash
                .role("ROLE_USER")
                .enabled(true)
                .build();

        // Step 3: Persist to PostgreSQL
        User savedUser = userRepository.save(user);
        log.info("New user registered: {} ({})", savedUser.getUsername(), savedUser.getEmail());

        // Step 4: Generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return buildAuthResponse(token, savedUser);
    }

    // ===== Login =====

    /**
     * Authenticate an existing user and return a JWT token.
     *
     * Steps:
     * 1. Authenticate via Spring Security AuthenticationManager
     * (verifies email + password, throws exception if invalid)
     * 2. Load user from DB
     * 3. Generate JWT token
     * 4. Return AuthResponse
     *
     * @param request LoginRequest DTO (email, password)
     * @return AuthResponse with JWT token and user info
     * @throws org.springframework.security.core.AuthenticationException if
     *                                                                   credentials
     *                                                                   are invalid
     */
    public AuthResponse login(LoginRequest request) {

        // Step 1: Authenticate — throws BadCredentialsException if wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        // Step 2: Load user from DB
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        log.info("User logged in: {}", user.getEmail());

        // Step 3: Generate JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        // Step 4: Return response
        return buildAuthResponse(token, user);
    }

    // ===== Helper =====

    private AuthResponse buildAuthResponse(String token, User user) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
