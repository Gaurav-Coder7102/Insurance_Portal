package com.insuranceportal.service;

import com.insuranceportal.dto.DocumentDto;
import com.insuranceportal.dto.HealthPolicyRequestDto;
import com.insuranceportal.dto.LifePolicyRequestDto;
import com.insuranceportal.dto.MotorPolicyRequestDto;
import com.insuranceportal.dto.PolicyDocumentsDto;
import com.insuranceportal.dto.PolicyResponseDto;
import com.insuranceportal.model.HealthPolicyDetail;
import com.insuranceportal.model.LifePolicyDetail;
import com.insuranceportal.model.MotorPolicyDetail;
import com.insuranceportal.model.PolicyDocument;
import com.insuranceportal.model.PolicyRequest;
import com.insuranceportal.repository.HealthPolicyDetailRepository;
import com.insuranceportal.repository.LifePolicyDetailRepository;
import com.insuranceportal.repository.MotorPolicyDetailRepository;
import com.insuranceportal.repository.PolicyDocumentRepository;
import com.insuranceportal.repository.PolicyRequestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * PolicyService — central business logic for all insurance policy operations.
 *
 * Handles:
 * - Motor Insurance: save, update, delete
 * - Life Insurance: save, update, delete
 * - Health Insurance: save, update, delete
 *
 * All save/update operations are wrapped in a transaction to ensure
 * atomicity across policy_requests + detail tables + policy_documents.
 */
@Service
@RequiredArgsConstructor
public class PolicyService {

        private final PolicyRequestRepository policyRequestRepository;
        private final MotorPolicyDetailRepository motorPolicyDetailRepository;
        private final LifePolicyDetailRepository lifePolicyDetailRepository;
        private final HealthPolicyDetailRepository healthPolicyDetailRepository;
        private final PolicyDocumentRepository policyDocumentRepository;

        // =========================================================
        // MOTOR INSURANCE
        // =========================================================

        /**
         * Save a new Motor Insurance policy request.
         *
         * Steps:
         * 1. Create and save PolicyRequest (parent record).
         * 2. Create and save MotorPolicyDetail linked to the request.
         * 3. Save all document metadata linked to the request.
         */
        @Transactional
        public PolicyResponseDto saveMotorPolicy(MotorPolicyRequestDto dto) {

                // 1. Build and save the parent PolicyRequest
                PolicyRequest policyRequest = PolicyRequest.builder()
                                .insuranceType("motor")
                                .name(dto.getName())
                                .email(dto.getEmail())
                                .phone(dto.getPhone())
                                .panNumber(dto.getPanCard())
                                .aadhaarNumber(dto.getAdharCard())
                                .gstNumber(dto.getGst())
                                .previousQuotesReceived(dto.getPreviousQuotesReceived())
                                .build();

                policyRequest = policyRequestRepository.save(policyRequest);

                // 2. Build and save motor-specific detail
                MotorPolicyDetail motorDetail = MotorPolicyDetail.builder()
                                .policyRequest(policyRequest)
                                .vehicleNumber(dto.getVehicleNumber())
                                .vehicleType(dto.getVehicleType())
                                .quoteRequest(dto.getQuoteRequest())
                                .policyType(dto.getPolicyType())
                                .previousInsuranceCompany(dto.getPreviousInsuranceCompany())
                                .rcBookNumber(dto.getPreviousRcBook())
                                .build();

                motorPolicyDetailRepository.save(motorDetail);

                // 3. Save document metadata
                saveDocuments(policyRequest, dto.getDocuments());

                return PolicyResponseDto.builder()
                                .id(policyRequest.getId())
                                .insuranceType("motor")
                                .message("Motor policy saved successfully")
                                .build();
        }

        /**
         * Update an existing Motor Insurance policy request by policy_request_id.
         *
         * Steps:
         * 1. Load and update the PolicyRequest fields.
         * 2. Load and update the MotorPolicyDetail fields.
         * 3. Replace all existing documents with new document list.
         */
        @Transactional
        public PolicyResponseDto updateMotorPolicy(Long id, MotorPolicyRequestDto dto) {

                // 1. Update PolicyRequest
                PolicyRequest policyRequest = policyRequestRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Motor policy not found with id: " + id));

                updateCommonFields(policyRequest, dto.getName(), dto.getEmail(), dto.getPhone(),
                                dto.getPanCard(), dto.getAdharCard(), dto.getGst(), dto.getPreviousQuotesReceived());
                policyRequestRepository.save(policyRequest);

                // 2. Update motor detail
                MotorPolicyDetail motorDetail = motorPolicyDetailRepository
                                .findByPolicyRequestId(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Motor policy detail not found for request id: " + id));

                motorDetail.setVehicleNumber(dto.getVehicleNumber());
                motorDetail.setVehicleType(dto.getVehicleType());
                motorDetail.setQuoteRequest(dto.getQuoteRequest());
                motorDetail.setPolicyType(dto.getPolicyType());
                motorDetail.setPreviousInsuranceCompany(dto.getPreviousInsuranceCompany());
                motorDetail.setRcBookNumber(dto.getPreviousRcBook());
                motorPolicyDetailRepository.save(motorDetail);

                // 3. Replace documents
                replaceDocuments(policyRequest, dto.getDocuments());

                return PolicyResponseDto.builder()
                                .id(id)
                                .insuranceType("motor")
                                .message("Motor policy updated successfully")
                                .build();
        }

        // =========================================================
        // LIFE INSURANCE
        // =========================================================

        /**
         * Save a new Life Insurance policy request.
         */
        @Transactional
        public PolicyResponseDto saveLifePolicy(LifePolicyRequestDto dto) {

                PolicyRequest policyRequest = PolicyRequest.builder()
                                .insuranceType("life")
                                .name(dto.getName())
                                .email(dto.getEmail())
                                .phone(dto.getPhone())
                                .panNumber(dto.getPanCard())
                                .aadhaarNumber(dto.getAdharCard())
                                .gstNumber(dto.getGst())
                                .previousQuotesReceived(dto.getPreviousQuotesReceived())
                                .build();

                policyRequest = policyRequestRepository.save(policyRequest);

                LifePolicyDetail lifeDetail = LifePolicyDetail.builder()
                                .policyRequest(policyRequest)
                                .productName(dto.getSelectProduct())
                                .policyCategory(dto.getOtherPolicyType())
                                .previousInsuranceCompany(dto.getPreviousInsuranceCompany())
                                .build();

                lifePolicyDetailRepository.save(lifeDetail);

                saveDocuments(policyRequest, dto.getDocuments());

                return PolicyResponseDto.builder()
                                .id(policyRequest.getId())
                                .insuranceType("life")
                                .message("Life policy saved successfully")
                                .build();
        }

        /**
         * Update an existing Life Insurance policy request.
         */
        @Transactional
        public PolicyResponseDto updateLifePolicy(Long id, LifePolicyRequestDto dto) {

                PolicyRequest policyRequest = policyRequestRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Life policy not found with id: " + id));

                updateCommonFields(policyRequest, dto.getName(), dto.getEmail(), dto.getPhone(),
                                dto.getPanCard(), dto.getAdharCard(), dto.getGst(), dto.getPreviousQuotesReceived());
                policyRequestRepository.save(policyRequest);

                LifePolicyDetail lifeDetail = lifePolicyDetailRepository
                                .findByPolicyRequestId(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Life policy detail not found for request id: " + id));

                lifeDetail.setProductName(dto.getSelectProduct());
                lifeDetail.setPolicyCategory(dto.getOtherPolicyType());
                lifeDetail.setPreviousInsuranceCompany(dto.getPreviousInsuranceCompany());
                lifePolicyDetailRepository.save(lifeDetail);

                replaceDocuments(policyRequest, dto.getDocuments());

                return PolicyResponseDto.builder()
                                .id(id)
                                .insuranceType("life")
                                .message("Life policy updated successfully")
                                .build();
        }

        // =========================================================
        // HEALTH INSURANCE
        // =========================================================

        /**
         * Save a new Health Insurance policy request.
         */
        @Transactional
        public PolicyResponseDto saveHealthPolicy(HealthPolicyRequestDto dto) {

                PolicyRequest policyRequest = PolicyRequest.builder()
                                .insuranceType("health")
                                .name(dto.getName())
                                .email(dto.getEmail())
                                .phone(dto.getPhone())
                                .panNumber(dto.getPanCard())
                                .aadhaarNumber(dto.getAdharCard())
                                .gstNumber(dto.getGst())
                                .previousQuotesReceived(dto.getPreviousQuotesReceived())
                                .build();

                policyRequest = policyRequestRepository.save(policyRequest);

                HealthPolicyDetail healthDetail = HealthPolicyDetail.builder()
                                .policyRequest(policyRequest)
                                .productName(dto.getSelectProduct())
                                .policyCategory(dto.getOtherPolicyType())
                                .previousInsuranceCompany(dto.getPreviousInsuranceCompany())
                                .build();

                healthPolicyDetailRepository.save(healthDetail);

                saveDocuments(policyRequest, dto.getDocuments());

                return PolicyResponseDto.builder()
                                .id(policyRequest.getId())
                                .insuranceType("health")
                                .message("Health policy saved successfully")
                                .build();
        }

        /**
         * Update an existing Health Insurance policy request.
         */
        @Transactional
        public PolicyResponseDto updateHealthPolicy(Long id, HealthPolicyRequestDto dto) {

                PolicyRequest policyRequest = policyRequestRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Health policy not found with id: " + id));

                updateCommonFields(policyRequest, dto.getName(), dto.getEmail(), dto.getPhone(),
                                dto.getPanCard(), dto.getAdharCard(), dto.getGst(), dto.getPreviousQuotesReceived());
                policyRequestRepository.save(policyRequest);

                HealthPolicyDetail healthDetail = healthPolicyDetailRepository
                                .findByPolicyRequestId(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Health policy detail not found for request id: " + id));

                healthDetail.setProductName(dto.getSelectProduct());
                healthDetail.setPolicyCategory(dto.getOtherPolicyType());
                healthDetail.setPreviousInsuranceCompany(dto.getPreviousInsuranceCompany());
                healthPolicyDetailRepository.save(healthDetail);

                replaceDocuments(policyRequest, dto.getDocuments());

                return PolicyResponseDto.builder()
                                .id(id)
                                .insuranceType("health")
                                .message("Health policy updated successfully")
                                .build();
        }

        // =========================================================
        // DELETE — shared across all insurance types
        // =========================================================

        /**
         * Delete any policy request by id.
         * CASCADE on the DB schema handles deletion of child records
         * (motor/life/health details + documents).
         */
        @Transactional
        public PolicyResponseDto deletePolicy(Long id, String insuranceType) {

                PolicyRequest policyRequest = policyRequestRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                Character.toUpperCase(insuranceType.charAt(0))
                                                                + insuranceType.substring(1)
                                                                + " policy not found with id: " + id));

                policyRequestRepository.delete(policyRequest);

                return PolicyResponseDto.builder()
                                .id(id)
                                .insuranceType(insuranceType)
                                .message(Character.toUpperCase(insuranceType.charAt(0)) + insuranceType.substring(1)
                                                + " policy deleted successfully")
                                .build();
        }

        // =========================================================
        // PRIVATE HELPERS
        // =========================================================

        /**
         * Update common PolicyRequest fields in-place.
         */
        private void updateCommonFields(PolicyRequest req, String name, String email, String phone,
                        String panCard, String adharCard, String gst,
                        String previousQuotes) {
                req.setName(name);
                req.setEmail(email);
                req.setPhone(phone);
                req.setPanNumber(panCard);
                req.setAadhaarNumber(adharCard);
                req.setGstNumber(gst);
                req.setPreviousQuotesReceived(previousQuotes);
        }

        /**
         * Save document metadata from a PolicyDocumentsDto (named-object format).
         *
         * The frontend sends documents as a named object:
         * { "rcBookDocument": {...}, "panDocument": {...}, ... }
         *
         * Each field is mapped to a PolicyDocument row with a descriptive documentType.
         * Null fields (e.g. rcBookDocument for life/health) are safely skipped.
         */
        private void saveDocuments(PolicyRequest policyRequest, PolicyDocumentsDto docs) {
                if (docs == null)
                        return;

                List<PolicyDocument> documents = new ArrayList<>();

                addDocumentIfPresent(documents, policyRequest, "rcBook", docs.getRcBookDocument());
                addDocumentIfPresent(documents, policyRequest, "pan", docs.getPanDocument());
                addDocumentIfPresent(documents, policyRequest, "aadhaar", docs.getAadhaarCardDocument());
                addDocumentIfPresent(documents, policyRequest, "previousInsuranceQuote",
                                docs.getPreviousInsuranceQuoteDocument());

                if (!documents.isEmpty()) {
                        policyDocumentRepository.saveAll(documents);
                }
        }

        /**
         * Adds a single PolicyDocument to the list if the given DocumentDto is not
         * null.
         */
        private void addDocumentIfPresent(List<PolicyDocument> list, PolicyRequest policyRequest,
                        String documentType, DocumentDto doc) {
                if (doc == null)
                        return;
                list.add(PolicyDocument.builder()
                                .policyRequest(policyRequest)
                                .documentType(documentType)
                                .originalName(doc.getOriginalName())
                                .mimeType(doc.getMimeType())
                                .fileSize(doc.getSize())
                                .build());
        }

        /**
         * Replace all existing documents for a PolicyRequest with new ones.
         * Used during update operations.
         */
        private void replaceDocuments(PolicyRequest policyRequest, PolicyDocumentsDto docs) {
                policyDocumentRepository.deleteByPolicyRequestId(policyRequest.getId());
                saveDocuments(policyRequest, docs);
        }
}
