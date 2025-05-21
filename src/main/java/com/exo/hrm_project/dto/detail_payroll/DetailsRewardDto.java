package com.exo.hrm_project.dto.detail_payroll;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.utils.enums.DeductionType;
import java.util.List;
import lombok.Data;

@Data
public class DetailsRewardDto {

  private Long id;
  private String code;
  private String name;
  private CommonDto groupReward;
  private List<DeductionType> includeType;
}
