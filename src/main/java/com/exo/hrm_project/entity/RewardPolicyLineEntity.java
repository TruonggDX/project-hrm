package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.Cycle;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "reward_policy_line")
public class RewardPolicyLineEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private Cycle cycle;
  private BigDecimal amount;
  private Long rewardPolicyId;
  private Long rewardId;
}
