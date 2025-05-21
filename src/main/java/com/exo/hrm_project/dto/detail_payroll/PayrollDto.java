package com.exo.hrm_project.dto.detail_payroll;

import com.exo.hrm_project.dto.common.CommonDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class PayrollDto {

  private Long id;
  private CommonDto employee;
  private CommonDto department;
  private CommonDto position;
  private String cycle;
  private BigDecimal totalAllowanceAmount;
  private LocalDate startDate;
  private String note;
  private GroupAllowancesDto groupAllowances;
  private GroupRewardsDto groupRewards;
}
