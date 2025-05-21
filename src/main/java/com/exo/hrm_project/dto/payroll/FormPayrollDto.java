package com.exo.hrm_project.dto.payroll;

import com.exo.hrm_project.dto.common.CommonDto;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class FormPayrollDto {

  private Long id;
  private CommonDto employee;
  private CommonDto department;
  private CommonDto position;
  private String type;
  private String cycle;
  private BigDecimal totalAllowanceAmount;
  private LocalDate startDate;
  private String note;
  private List<FormPayrollLineDto> payrollLine;
}
