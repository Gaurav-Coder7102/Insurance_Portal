package com.insuranceportal.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * PolicyDocument — stores metadata of uploaded documents.
 * Maps to: policy_documents
 *
 * document_type values:
 * - rcBook
 * - pan
 * - aadhaar
 * - previousInsuranceQuote
 */
@Entity
@Table(name = "policy_documents")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "policy_request_id", nullable = false)
    private PolicyRequest policyRequest;

    /** rcBook | pan | aadhaar | previousInsuranceQuote */
    @Column(name = "document_type", length = 50)
    private String documentType;

    @Column(name = "original_name", length = 255)
    private String originalName;

    @Column(name = "mime_type", length = 100)
    private String mimeType;

    @Column(name = "file_size")
    private Long fileSize;
}
