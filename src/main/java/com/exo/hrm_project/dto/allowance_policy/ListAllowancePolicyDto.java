package com.exo.hrm_project.dto.allowance_policy;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ListAllowancePolicyDto {

  private Long id;
  private String code;
  private String name;
  private String applicableType;
  private String type;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
  private String state;
}
