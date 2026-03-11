package com.insuranceportal.repository;

import com.insuranceportal.model.MotorPolicyDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MotorPolicyDetailRepository extends JpaRepository<MotorPolicyDetail, Long> {

    Optional<MotorPolicyDetail> findByPolicyRequestId(Long policyRequestId);
}
