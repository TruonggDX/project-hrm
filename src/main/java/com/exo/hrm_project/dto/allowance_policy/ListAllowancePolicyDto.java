package com.exo.hrm_project.dto.allowance_policy;

import java.time.LocalDate;
import lombok.Data;

@Data
public class ListAllowancePolicyDto {

  private Long id;
  private String code;
  private String name;
  private String applicableType;
  private String type;
  private LocalDate startDate;
  private LocalDate endDate;
  private String state;
}
