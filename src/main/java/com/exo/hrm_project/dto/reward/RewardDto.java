package com.exo.hrm_project.dto.reward;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.utils.enums.AllowanceRewardType;
import com.exo.hrm_project.utils.enums.DeductionType;
import java.util.List;
import lombok.Data;

@Data
public class RewardDto {

  private Long id;
  private String code;
  private String name;
  private CommonDto groupReward;
  private AllowanceRewardType type;
  private CommonDto uom;
  private CommonDto currency;
  private List<DeductionType> includeType;
  private String description;
}
