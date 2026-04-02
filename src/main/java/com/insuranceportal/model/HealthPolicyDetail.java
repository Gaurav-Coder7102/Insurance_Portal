package com.insuranceportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * HealthPolicyDetail — stores health-insurance-specific fields.
 * Maps to: health_policy_details
 *
 * FK → policy_requests (UNIQUE — one-to-one)
 */
@Entity
@Table(name = "health_policy_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthPolicyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_request_id", nullable = false, unique = true)
    @JsonIgnore
    private PolicyRequest policyRequest;

    /** e.g. health-plus, critical-illness, super-top-up */
    @Column(name = "product_name", length = 100)
    private String productName;

    /** individual | family-floater | senior-citizen */
    @Column(name = "policy_category", length = 50)
    private String policyCategory;

    @Column(name = "previous_insurance_company", length = 150)
    private String previousInsuranceCompany;
}
