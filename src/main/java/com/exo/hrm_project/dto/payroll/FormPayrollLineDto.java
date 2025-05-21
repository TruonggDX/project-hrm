package com.exo.hrm_project.dto.payroll;

import com.exo.hrm_project.dto.common.CommonDto;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class FormPayrollLineDto {

  private Long id;
  private String type;
  private CommonDto groupTarget;
  private CommonDto target;
  private String amountItem;
  private BigDecimal amount;
  private BigDecimal taxableAmount;
  private BigDecimal insuranceAmount;

}
