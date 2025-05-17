package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.AllowancePolicyApplicableTargetEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowancePolicyApplicableTargetRepository extends
    JpaRepository<AllowancePolicyApplicableTargetEntity, Long> {

  @Query("SELECT a FROM AllowancePolicyApplicableTargetEntity a WHERE a.allowancePolicyId=:allowancePolicyId")
  List<AllowancePolicyApplicableTargetEntity> findByAllowancePolicyId(Long allowancePolicyId);
}
