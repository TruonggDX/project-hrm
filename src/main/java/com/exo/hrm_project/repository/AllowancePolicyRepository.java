package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.AllowancePolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowancePolicyRepository extends JpaRepository<AllowancePolicyEntity, Long>,
    JpaSpecificationExecutor<AllowancePolicyEntity> {

}
