package com.exo.hrm_project.dto.allowance_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class AllowancePolicyDto {

  private Long id;
  private String code;
  private String name;
  private String applicableType;
  private String type;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
  private String description;
  private List<CommonDto> target;
  private List<AllowancePolicyLineDto> allowancePolicyLine;
}
