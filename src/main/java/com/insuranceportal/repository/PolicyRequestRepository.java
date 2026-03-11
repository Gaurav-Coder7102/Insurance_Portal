package com.insuranceportal.repository;

import com.insuranceportal.model.PolicyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyRequestRepository extends JpaRepository<PolicyRequest, Long> {

    List<PolicyRequest> findByInsuranceType(String insuranceType);

    List<PolicyRequest> findByEmail(String email);
}
