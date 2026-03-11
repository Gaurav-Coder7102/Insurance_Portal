package com.insuranceportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LifePolicyRequestDto — incoming payload for Life Insurance requests.
 *
 * Maps to:
 * - policy_requests (common fields)
 * - life_policy_details (life-specific fields)
 * - policy_documents (document metadata list)
 *
 * Example JSON:
 * {
 * "name": "John Doe",
 * "email": "john@example.com",
 * "phone": "9876543210",
 * "selectProduct": "term-plan",
 * "otherPolicyType": "individual",
 * "previousInsuranceCompany": "ABC Insurance",
 * "panCard": "ABCDE1234F",
 * "gst": "27ABCDE1234F1Z5",
 * "adharCard": "123412341234",
 * "previousQuotesReceived": "Old life quote reference",
 * "documents": [ {...}, {...} ]
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LifePolicyRequestDto {

    // ===== Common fields =====

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private String phone;

    private String panCard;

    private String adharCard;

    private String gst;

    private String previousQuotesReceived;

    // ===== Life-specific fields =====

    /** term-plan | ulip | endowment | money-back | whole-life */
    private String selectProduct;

    /** individual | joint | group */
    private String otherPolicyType;

    private String previousInsuranceCompany;

    // ===== Documents =====

    /**
     * Named document object matching the frontend's payload format.
     * Fields: panDocument, aadhaarCardDocument, previousInsuranceQuoteDocument
     * rcBookDocument is null / omitted for life insurance.
     */
    private PolicyDocumentsDto documents;
}
