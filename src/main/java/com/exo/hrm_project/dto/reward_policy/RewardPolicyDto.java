package com.exo.hrm_project.dto.reward_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class RewardPolicyDto {

  private Long id;
  private String code;
  private String name;
  private String applicableType;
  private String type;
  private LocalDate startDate;
  private LocalDate endDate;
  private String description;
  private String state;
  private List<CommonDto> targets;
  private List<RewardPolicyLineDto> rewardPolicyLines;
}
