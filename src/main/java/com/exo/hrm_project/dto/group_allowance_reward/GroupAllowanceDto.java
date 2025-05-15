package com.exo.hrm_project.dto.group_allowance_reward;

import com.exo.hrm_project.dto.common.CommonDto;
import lombok.Data;

@Data
public class GroupAllowanceDto {

  private Long id;
  private String code;
  private String name;
  private String description;
  private Boolean isActive;
  private CommonDto parent;
}
