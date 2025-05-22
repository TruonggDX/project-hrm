package com.exo.hrm_project.dto.allowance_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class AllowancePolicyDto {

  private Long id;
  private String code;
  private String name;
  private String applicableType;
  private String type;
  private LocalDate startDate;
  private LocalDate endDate;
  private String description;
  private List<CommonDto> targets;
  private List<AllowancePolicyLineDto> allowancePolicyLines;
}
