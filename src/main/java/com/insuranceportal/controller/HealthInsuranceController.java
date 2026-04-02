package com.insuranceportal.controller;

import com.insuranceportal.dto.HealthPolicyRequestDto;
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
 * HealthInsuranceController — REST endpoints for Health Insurance policy
 * management.
 *
 * Endpoints:
 * POST /api/insurance/health → Save new health policy
 * PUT /api/insurance/health/{id} → Update existing health policy
 * DELETE /api/insurance/health/{id} → Delete health policy
 *
 * Authentication: JWT Bearer token required (via Spring Security filter).
 */
@RestController
@RequestMapping("/api/insurance/health")
@RequiredArgsConstructor
@Tag(name = "Health Insurance", description = "APIs for managing Health Insurance policy requests")
public class HealthInsuranceController {

    private final PolicyService policyService;

    // ---------------------------------------------------------
    // POST /api/insurance/health
    // Save a new Health Insurance policy request
    // ---------------------------------------------------------
    @PostMapping(consumes = {"multipart/form-data"})
    @Operation(summary = "Save Health Insurance Policy", description = "Creates a new health insurance policy request.")
    public ResponseEntity<PolicyResponseDto> saveHealthPolicy(
            @Valid @ModelAttribute HealthPolicyRequestDto dto) {

        PolicyResponseDto response = policyService.saveHealthPolicy(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ---------------------------------------------------------
    // PUT /api/insurance/health/{id}
    // Update an existing Health Insurance policy request
    // ---------------------------------------------------------
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @Operation(summary = "Update Health Insurance Policy", description = "Updates an existing health insurance policy request.")
    public ResponseEntity<PolicyResponseDto> updateHealthPolicy(
            @Parameter(description = "ID of the policy request to update", required = true) @PathVariable Long id,
            @Valid @ModelAttribute HealthPolicyRequestDto dto) {

        PolicyResponseDto response = policyService.updateHealthPolicy(id, dto);
        return ResponseEntity.ok(response);
    }

    // ---------------------------------------------------------
    // DELETE /api/insurance/health/{id}
    // Delete a Health Insurance policy request
    // ---------------------------------------------------------
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Health Insurance Policy", description = "Deletes a health insurance policy request and all associated health details and documents.")
    public ResponseEntity<PolicyResponseDto> deleteHealthPolicy(
            @Parameter(description = "ID of the policy request to delete", required = true) @PathVariable Long id) {

        PolicyResponseDto response = policyService.deletePolicy(id, "health");
        return ResponseEntity.ok(response);
    }
}
