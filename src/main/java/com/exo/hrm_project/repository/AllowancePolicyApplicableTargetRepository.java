package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.AllowancePolicyApplicableTargetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowancePolicyApplicableTargetRepository extends
    JpaRepository<AllowancePolicyApplicableTargetEntity, Long> {

}
