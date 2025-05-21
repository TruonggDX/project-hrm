package com.exo.hrm_project.dto.detail_payroll;

import java.util.List;
import lombok.Data;

@Data
public class GroupRewardsDto {

  private List<String> allGroupName;
  private List<DetailRewardDtoLine> reawardLine;
}
