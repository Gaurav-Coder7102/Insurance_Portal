package com.insuranceportal.controller;

import com.insuranceportal.dto.LifePolicyRequestDto;
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
 * LifeInsuranceController — REST endpoints for Life Insurance policy
 * management.
 *
 * Endpoints:
 * POST /api/insurance/life → Save new life policy
 * PUT /api/insurance/life/{id} → Update existing life policy
 * DELETE /api/insurance/life/{id} → Delete life policy
 *
 * Authentication: JWT Bearer token required (via Spring Security filter).
 */
@RestController
@RequestMapping("/api/insurance/life")
@RequiredArgsConstructor
@Tag(name = "Life Insurance", description = "APIs for managing Life Insurance policy requests")
public class LifeInsuranceController {

    private final PolicyService policyService;

    // ---------------------------------------------------------
    // POST /api/insurance/life
    // Save a new Life Insurance policy request
    // ---------------------------------------------------------
    @PostMapping
    @Operation(summary = "Save Life Insurance Policy", description = "Creates a new life insurance policy request with product, category, and document metadata.")
    public ResponseEntity<PolicyResponseDto> saveLifePolicy(
            @Valid @RequestBody LifePolicyRequestDto dto) {

        PolicyResponseDto response = policyService.saveLifePolicy(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------------------------------------
    // PUT /api/insurance/life/{id}
    // Update an existing Life Insurance policy request
    // ---------------------------------------------------------
    @PutMapping("/{id}")
    @Operation(summary = "Update Life Insurance Policy", description = "Updates an existing life insurance policy request identified by policy_request_id.")
    public ResponseEntity<PolicyResponseDto> updateLifePolicy(
            @Parameter(description = "ID of the policy request to update", required = true) @PathVariable Long id,
            @Valid @RequestBody LifePolicyRequestDto dto) {

        PolicyResponseDto response = policyService.updateLifePolicy(id, dto);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // DELETE /api/insurance/life/{id}
    // Delete a Life Insurance policy request
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Life Insurance Policy", description = "Deletes a life insurance policy request and all associated life details and documents.")
    public ResponseEntity<PolicyResponseDto> deleteLifePolicy(
            @Parameter(description = "ID of the policy request to delete", required = true) @PathVariable Long id) {

        PolicyResponseDto response = policyService.deletePolicy(id, "life");
        return ResponseEntity.ok(response);
    }
}
