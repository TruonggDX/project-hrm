package com.exo.hrm_project.dto.allowance_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class AllowancePolicyLineDto {

  private CommonDto allowance;
  private String cycle;
  private BigDecimal amount;
}
