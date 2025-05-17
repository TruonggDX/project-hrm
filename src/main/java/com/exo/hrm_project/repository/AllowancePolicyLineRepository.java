package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowancePolicyLineRepository extends
    JpaRepository<AllowancePolicyLineEntity, Long> {

  @Query("SELECT a FROM AllowancePolicyLineEntity a WHERE a.allowancePolicyId=:policyId")
  List<AllowancePolicyLineEntity> findByAllowancePolicyId(Long policyId);

}
