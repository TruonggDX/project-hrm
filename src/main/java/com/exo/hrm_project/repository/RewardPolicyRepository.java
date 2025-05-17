package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.RewardPolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPolicyRepository extends JpaRepository<RewardPolicyEntity, Long>,
    JpaSpecificationExecutor<RewardPolicyEntity> {

}
