package com.insuranceportal.controller;

import com.insuranceportal.dto.MotorPolicyRequestDto;
import com.insuranceportal.dto.PolicyResponseDto;
import com.insuranceportal.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * MotorInsuranceController — REST endpoints for Motor Insurance policy
 * management.
 *
 * Endpoints:
 * POST /api/insurance/motor → Save new motor policy
 * PUT /api/insurance/motor/{id} → Update existing motor policy
 * DELETE /api/insurance/motor/{id} → Delete motor policy
 *
 * Authentication: JWT Bearer token required (via Spring Security filter).
 */
@RestController
@RequestMapping("/api/insurance/motor")
@RequiredArgsConstructor
@Tag(name = "Motor Insurance", description = "APIs for managing Motor Insurance policy requests")
public class MotorInsuranceController {

    private final PolicyService policyService;

    // ---------------------------------------------------------
    // POST /api/insurance/motor
    // Save a new Motor Insurance policy request
    // ---------------------------------------------------------
    @PostMapping
    @Operation(summary = "Save Motor Insurance Policy", description = "Creates a new motor insurance policy request with vehicle details and document metadata.")
    public ResponseEntity<PolicyResponseDto> saveMotorPolicy(
            @Valid @RequestBody MotorPolicyRequestDto dto) {

        PolicyResponseDto response = policyService.saveMotorPolicy(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------------------------------------
    // PUT /api/insurance/motor/{id}
    // Update an existing Motor Insurance policy request
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Update Motor Insurance Policy", description = "Updates an existing motor insurance policy request identified by policy_request_id.")
    public ResponseEntity<PolicyResponseDto> updateMotorPolicy(
            @Parameter(description = "ID of the policy request to update", required = true) @PathVariable Long id,
            @Valid @RequestBody MotorPolicyRequestDto dto) {

        PolicyResponseDto response = policyService.updateMotorPolicy(id, dto);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // DELETE /api/insurance/motor/{id}
    // Delete a Motor Insurance policy request (cascades to detail + docs)
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Motor Insurance Policy", description = "Deletes a motor insurance policy request and all associated motor details and documents.")
    public ResponseEntity<PolicyResponseDto> deleteMotorPolicy(
            @Parameter(description = "ID of the policy request to delete", required = true) @PathVariable Long id) {

        PolicyResponseDto response = policyService.deletePolicy(id, "motor");
        return ResponseEntity.ok(response);
    }
}
