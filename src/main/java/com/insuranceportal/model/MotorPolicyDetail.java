package com.insuranceportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * MotorPolicyDetail — stores motor-specific insurance fields.
 * Maps to: motor_policy_details
 *
 * FK → policy_requests (UNIQUE — one-to-one)
 */
@Entity
@Table(name = "motor_policy_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotorPolicyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_request_id", nullable = false, unique = true)
    @JsonIgnore
    private PolicyRequest policyRequest;

    @Column(name = "vehicle_number", nullable = false, length = 20)
    private String vehicleNumber;

    @Column(name = "vehicle_type", length = 50)
    private String vehicleType;

    /** new-policy | renewal | portability */
    @Column(name = "quote_request", length = 50)
    private String quoteRequest;

    /** comprehensive | third-party | etc. */
    @Column(name = "policy_type", length = 50)
    private String policyType;

    @Column(name = "previous_insurance_company", length = 150)
    private String previousInsuranceCompany;

    @Column(name = "rc_book_number", length = 50)
    private String rcBookNumber;
}
