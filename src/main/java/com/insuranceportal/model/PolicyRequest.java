package com.insuranceportal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * PolicyRequest — parent record for any insurance policy request.
 * Maps to: policy_requests
 *
 * Relationships:
 * - OneToOne → MotorPolicyDetail (if motor)
 * - OneToOne → LifePolicyDetail (if life)
 * - OneToOne → HealthPolicyDetail (if health)
 * - OneToMany → PolicyDocument (uploaded document metadata)
 */
@Entity
@Table(name = "policy_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** motor | life | health */
    @Column(name = "insurance_type", nullable = false, length = 20)
    private String insuranceType;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "pan_number", length = 20)
    private String panNumber;

    @Column(name = "aadhaar_number", length = 20)
    private String aadhaarNumber;

    @Column(name = "gst_number", length = 20)
    private String gstNumber;

    @Column(name = "previous_quotes_received", columnDefinition = "TEXT")
    private String previousQuotesReceived;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ===== Relationships =====

    @OneToOne(mappedBy = "policyRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private MotorPolicyDetail motorPolicyDetail;

    @OneToOne(mappedBy = "policyRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private LifePolicyDetail lifePolicyDetail;

    @OneToOne(mappedBy = "policyRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private HealthPolicyDetail healthPolicyDetail;

    @OneToMany(mappedBy = "policyRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PolicyDocument> documents = new ArrayList<>();
}
