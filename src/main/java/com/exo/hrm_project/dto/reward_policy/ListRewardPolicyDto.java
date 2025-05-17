package com.exo.hrm_project.dto.reward_policy;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ListRewardPolicyDto {

  private Long id;
  private String code;
  private String name;
  private String applicableType;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
  private String type;
  private String state;
}
