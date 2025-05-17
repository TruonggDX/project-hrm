package com.exo.hrm_project.dto.reward_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class DetailRewardPolicyDto {

  private Long id;
  private String code;
  private String name;
  private String description;
  private String type;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;
  private String state;
  private String applicableType;
  private List<CommonDto> applicableTarget;
  private List<DetailRewardPolicyLineDto> rewardPolicyLine;
}
