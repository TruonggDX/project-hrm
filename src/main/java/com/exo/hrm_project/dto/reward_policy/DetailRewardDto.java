package com.exo.hrm_project.dto.reward_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.utils.enums.DeductionType;
import java.util.List;
import lombok.Data;

@Data
public class DetailRewardDto {

  private Long id;
  private String code;
  private String name;
  private List<DeductionType> includeTypes;
  private CommonDto groupReward;
  private CommonDto uom;
  private CommonDto currency;
}
