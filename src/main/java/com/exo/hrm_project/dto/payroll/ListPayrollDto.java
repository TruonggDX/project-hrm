package com.exo.hrm_project.dto.payroll;

import com.exo.hrm_project.dto.common.CommonDto;
import java.math.BigDecimal;
import java.util.List;
import lombok.Data;

@Data
public class ListPayrollDto {

  private Long id;
  private CommonDto employee;
  private CommonDto position;
  private String type;
  private String cycle;
  private BigDecimal totalAllowanceAmount;
  private List<PayrolleLineDto> payrollLines;
}
