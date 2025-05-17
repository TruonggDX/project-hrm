package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.RewardPolicyLineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardPolicyLineRepository extends JpaRepository<RewardPolicyLineEntity, Long> {

  @Query("SELECT r FROM RewardPolicyLineEntity r WHERE r.rewardPolicyId=:policyId")
  List<RewardPolicyLineEntity> findByRewardPolicyId(Long policyId);
}
