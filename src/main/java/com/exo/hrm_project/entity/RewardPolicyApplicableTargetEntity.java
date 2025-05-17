package com.exo.hrm_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "reward_policy_applicable_target")
public class RewardPolicyApplicableTargetEntity extends BaseEntity {

  private Long rewardPolicyId;
  private Long targetId;
}
