package com.insuranceportal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * User Entity — maps to the "users" table in PostgreSQL.
 *
 * Developer Notes:
 *   - Passwords are stored as BCrypt hashes (never plain text).
 *   - Role is stored as a plain String for simplicity ("ROLE_USER", "ROLE_ADMIN").
 *   - createdAt / updatedAt are automatically managed by Hibernate.
 */
@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email"),
        @UniqueConstraint(columnNames = "username")
    }
)
@Data                    // Lombok: generates getters, setters, toString, equals, hashCode
@Builder                 // Lombok: builder pattern
@NoArgsConstructor       // Lombok: no-arg constructor (required by JPA)
@AllArgsConstructor      // Lombok: all-arg constructor (used by builder)
public class User {

    // ===== Primary Key =====
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ===== User Info =====
    @NotBlank(message = "Username is required")
    @Column(nullable = false, length = 50)
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password; // BCrypt hashed

    // ===== Role =====
    // Values: "ROLE_USER" | "ROLE_ADMIN"
    @Column(nullable = false, length = 20)
    @Builder.Default
    private String role = "ROLE_USER";

    // ===== Status =====
    @Column(nullable = false)
    @Builder.Default
    private boolean enabled = true;

    // ===== Audit Timestamps =====
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
