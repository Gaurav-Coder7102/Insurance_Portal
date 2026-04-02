package com.insuranceportal.controller;

import com.insuranceportal.model.PolicyRequest;
import com.insuranceportal.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/quotes-requests")
@RequiredArgsConstructor
@Tag(name = "Quotes Requests", description = "API for retrieving policy quote requests")
public class QuoteRequestController {

    private final PolicyService policyService;

    @GetMapping
    @Operation(summary = "Get all quotes requests", description = "Retrieves a list of all insurance quote requests including the date requested and respective policy details.")
    public ResponseEntity<List<PolicyRequest>> getAllQuoteRequests() {
        return ResponseEntity.ok(policyService.getAllQuoteRequests());
    }
}
