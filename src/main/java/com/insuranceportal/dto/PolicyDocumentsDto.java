package com.insuranceportal.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PolicyDocumentsDto — matches the "documents" object sent from the frontend.
 *
 * The frontend sends documents as a named object (NOT an array).
 * Any field can be null if the document is not applicable for that insurance
 * type.
 *
 * Example (Motor):
 * {
 * "rcBookDocument": { "originalName": "rc-book.pdf", "mimeType":
 * "application/pdf", "size": 245678 },
 * "panDocument": { "originalName": "pan-card.jpg", "mimeType": "image/jpeg",
 * "size": 104220 },
 * "aadhaarCardDocument": { "originalName": "aadhaar.png", "mimeType":
 * "image/png", "size": 223901 },
 * "previousInsuranceQuoteDocument": { "originalName": "quote.pdf","mimeType":
 * "application/pdf", "size": 331245 }
 * }
 *
 * Example (Life / Health):
 * rcBookDocument is null or omitted — all other fields same as above.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PolicyDocumentsDto {

    /** RC Book — only present for Motor Insurance */
    private DocumentDto rcBookDocument;

    /** PAN Card — all insurance types */
    private DocumentDto panDocument;

    /** Aadhaar Card — all insurance types */
    private DocumentDto aadhaarCardDocument;

    /** Previous insurance quote — all insurance types */
    private DocumentDto previousInsuranceQuoteDocument;
}
