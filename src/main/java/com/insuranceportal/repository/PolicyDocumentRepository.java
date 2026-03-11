package com.insuranceportal.repository;

import com.insuranceportal.model.PolicyDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyDocumentRepository extends JpaRepository<PolicyDocument, Long> {

    List<PolicyDocument> findByPolicyRequestId(Long policyRequestId);

    void deleteByPolicyRequestId(Long policyRequestId);
}
