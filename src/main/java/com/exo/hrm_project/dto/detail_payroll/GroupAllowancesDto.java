package com.exo.hrm_project.dto.detail_payroll;

import java.util.List;
import lombok.Data;

@Data
public class GroupAllowancesDto {

  private List<String> allGroupName;
  private List<AllowanceDtoLine> allowanceLine;
}
