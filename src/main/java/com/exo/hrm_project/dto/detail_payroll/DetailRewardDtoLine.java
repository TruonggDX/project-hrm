package com.exo.hrm_project.dto.detail_payroll;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DetailRewardDtoLine {

  private Long id;
  private DetailsRewardDto reward;
  private BigDecimal amount;
  private String amountItem;
  private BigDecimal taxableAmount;
  private BigDecimal insuranceAmount;
}
