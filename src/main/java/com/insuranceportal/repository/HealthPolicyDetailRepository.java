package com.insuranceportal.repository;

import com.insuranceportal.model.HealthPolicyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthPolicyDetailRepository extends JpaRepository<HealthPolicyDetail, Long> {

    Optional<HealthPolicyDetail> findByPolicyRequestId(Long policyRequestId);
}
