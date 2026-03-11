package com.insuranceportal.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PolicyResponseDto — standard API response for save / update / delete
 * operations.
 *
 * Example responses:
 * Save: { "id": 1, "insuranceType": "motor", "message": "Motor policy saved
 * successfully" }
 * Update: { "id": 1, "insuranceType": "motor", "message": "Motor policy updated
 * successfully" }
 * Delete: { "id": 1, "insuranceType": "motor", "message": "Motor policy deleted
 * successfully" }
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResponseDto {

    /** The ID of the PolicyRequest record */
    private Long id;

    /** motor | life | health */
    private String insuranceType;

    /** Human-readable result message */
    private String message;
}
