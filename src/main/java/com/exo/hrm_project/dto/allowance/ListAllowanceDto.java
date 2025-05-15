package com.exo.hrm_project.dto.allowance;

import com.exo.hrm_project.dto.common.CommonDto;
import lombok.Data;

@Data
public class ListAllowanceDto {

  private Long id;
  private String code;
  private String name;
  private String description;
  private Boolean isActive;
  private CommonDto groupAllowance;
}
