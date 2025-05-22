package com.exo.hrm_project.dto.allowance_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DetailAllowancePolicyDto {

  private Long id;
  private String code;
  private String name;
  private String description;
  private String type;
  private LocalDate startDate;
  private LocalDate endDate;
  private String state;
  private String applicableType;
  private List<CommonDto> applicableTargets;
  private List<DetailAllowancePolicyLineDto> allowancePolicyLines;
}
