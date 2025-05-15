package com.exo.hrm_project.dto.group_allowance_reward;

import lombok.Data;

@Data
public class ListGroupAllowanceDto {

  private Long id;
  private String code;
  private String name;
  private String description;
  private Boolean isActive;
}
