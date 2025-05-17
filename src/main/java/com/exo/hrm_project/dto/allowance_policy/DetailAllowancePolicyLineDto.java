package com.exo.hrm_project.dto.allowance_policy;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DetailAllowancePolicyLineDto {

  private Long id;
  private String cycle;
  private BigDecimal amount;
  private DetailAllowanceDto allowance;
}
