package com.exo.hrm_project.dto.detail_payroll;

import com.exo.hrm_project.dto.common.CommonDto;
import java.util.List;
import lombok.Data;

@Data
public class GroupRewardsDto {

  private List<CommonDto> groupRewards;
  private List<DetailRewardDtoLine> rewardLines;
}
