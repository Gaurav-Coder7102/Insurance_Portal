package com.insuranceportal.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * LifePolicyDetail — stores life-insurance-specific fields.
 * Maps to: life_policy_details
 *
 * FK → policy_requests (UNIQUE — one-to-one)
 */
@Entity
@Table(name = "life_policy_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LifePolicyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_request_id", nullable = false, unique = true)
    @JsonIgnore
    private PolicyRequest policyRequest;

    /** e.g. term-plan, ulip, endowment */
    @Column(name = "product_name", length = 100)
    private String productName;

    /** individual | joint | group */
    @Column(name = "policy_category", length = 50)
    private String policyCategory;

    @Column(name = "previous_insurance_company", length = 150)
    private String previousInsuranceCompany;
}
