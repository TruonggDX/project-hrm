package com.exo.hrm_project.dto.reward_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class RewardPolicyLineDto {

  private Long id;
  private CommonDto reward;
  private String cycle;
  private BigDecimal amount;
}
