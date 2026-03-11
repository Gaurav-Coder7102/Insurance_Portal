package com.insuranceportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DocumentDto — represents metadata for a single uploaded document.
 * Used as a nested object inside each policy request DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {

    /** rcBook | pan | aadhaar | previousInsuranceQuote */
    private String documentType;

    private String originalName;

    private String mimeType;

    private Long size;
}
