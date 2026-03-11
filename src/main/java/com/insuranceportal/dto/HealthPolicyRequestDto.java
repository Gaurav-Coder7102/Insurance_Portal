package com.insuranceportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * HealthPolicyRequestDto — incoming payload for Health Insurance requests.
 *
 * Maps to:
 * - policy_requests (common fields)
 * - health_policy_details (health-specific fields)
 * - policy_documents (document metadata list)
 *
 * Example JSON:
 * {
 * "name": "John Doe",
 * "email": "john@example.com",
 * "phone": "9876543210",
 * "selectProduct": "health-plus",
 * "otherPolicyType": "family-floater",
 * "previousInsuranceCompany": "ABC Insurance",
 * "panCard": "ABCDE1234F",
 * "gst": "27ABCDE1234F1Z5",
 * "adharCard": "123412341234",
 * "previousQuotesReceived": "Old health quote reference",
 * "documents": [ {...}, {...} ]
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthPolicyRequestDto {

    // ===== Common fields =====

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private String phone;

    private String panCard;

    private String adharCard;

    private String gst;

    private String previousQuotesReceived;

    // ===== Health-specific fields =====

    /** health-plus | critical-illness | super-top-up | senior-citizen */
    private String selectProduct;

    /** individual | family-floater | senior-citizen */
    private String otherPolicyType;

    private String previousInsuranceCompany;

    // ===== Documents =====

    /**
     * Named document object matching the frontend's payload format.
     * Fields: panDocument, aadhaarCardDocument, previousInsuranceQuoteDocument
     * rcBookDocument is null / omitted for health insurance.
     */
    private PolicyDocumentsDto documents;
}
