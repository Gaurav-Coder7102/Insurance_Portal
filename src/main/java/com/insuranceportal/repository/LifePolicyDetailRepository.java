package com.insuranceportal.repository;

import com.insuranceportal.model.LifePolicyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LifePolicyDetailRepository extends JpaRepository<LifePolicyDetail, Long> {

    Optional<LifePolicyDetail> findByPolicyRequestId(Long policyRequestId);
}
