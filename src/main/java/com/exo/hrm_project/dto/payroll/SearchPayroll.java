package com.exo.hrm_project.dto.payroll;

import lombok.Data;

@Data
public class SearchPayroll {

  private String codeEmployee;
  private String nameEmployee;
  private String position;
  private String cycleSalary;
}
