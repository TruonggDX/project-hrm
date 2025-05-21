package com.exo.hrm_project.dto.payroll;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PayrolleLineDto {

  private String targetName;
  private BigDecimal amount;
  private BigDecimal taxableAmount;
  private BigDecimal insuranceAmount;
  private TargetPolicyLine targetPolicyLine;
}
