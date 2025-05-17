package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.RewardPolicyApplicableTargetEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPolicyApplicableTargetRepository extends
    JpaRepository<RewardPolicyApplicableTargetEntity, Long> {

  @Query("SELECT a FROM RewardPolicyApplicableTargetEntity a WHERE a.rewardPolicyId=:rewardPolicyId")
  List<RewardPolicyApplicableTargetEntity> findByRewardPolicyId(Long rewardPolicyId);
}
