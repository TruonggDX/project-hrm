package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowancePolicyLineRepository extends
    JpaRepository<AllowancePolicyLineEntity, Long> {

}
