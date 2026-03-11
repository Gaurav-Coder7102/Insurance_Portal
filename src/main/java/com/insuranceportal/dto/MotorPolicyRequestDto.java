package com.insuranceportal.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * MotorPolicyRequestDto — incoming payload for Motor Insurance requests.
 *
 * Maps to:
 * - policy_requests (common fields)
 * - motor_policy_details (motor-specific fields)
 * - policy_documents (document metadata)
 *
 * The "documents" field is a named object (not an array) matching
 * the structure sent by the frontend:
 * {
 * "rcBookDocument": { "originalName": "rc-book.pdf", "mimeType":
 * "application/pdf", "size": 245678 },
 * "panDocument": { "originalName": "pan-card.jpg", "mimeType": "image/jpeg",
 * "size": 104220 },
 * "aadhaarCardDocument": { "originalName": "aadhaar.png", "mimeType":
 * "image/png", "size": 223901 },
 * "previousInsuranceQuoteDocument": { "originalName": "quote.pdf", "mimeType":
 * "application/pdf", "size": 331245 }
 * }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MotorPolicyRequestDto {

    // ===== Common fields =====

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    private String phone;

    private String panCard;

    private String adharCard;

    private String gst;

    private String previousQuotesReceived;

    // ===== Motor-specific fields =====

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    /** two-wheeler | four-wheeler | commercial */
    private String vehicleType;

    /** new-policy | renewal | portability */
    private String quoteRequest;

    /** comprehensive | third-party | own-damage */
    private String policyType;

    private String previousInsuranceCompany;

    /** RC Book number */
    private String previousRcBook;

    // ===== Documents =====

    /**
     * Named document object matching the frontend's payload format.
     * Fields: rcBookDocument, panDocument, aadhaarCardDocument,
     * previousInsuranceQuoteDocument
     * Null fields are safely ignored.
     */
    private PolicyDocumentsDto documents;
}
