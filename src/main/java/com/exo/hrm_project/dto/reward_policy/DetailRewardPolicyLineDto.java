package com.exo.hrm_project.dto.reward_policy;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DetailRewardPolicyLineDto {

  private Long id;
  private String cycle;
  private BigDecimal amount;
  private DetailRewardDto reward;
}
