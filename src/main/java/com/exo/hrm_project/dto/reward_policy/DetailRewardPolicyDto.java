package com.exo.hrm_project.dto.reward_policy;

import com.exo.hrm_project.dto.common.CommonDto;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class DetailRewardPolicyDto {

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
  private List<DetailRewardPolicyLineDto> rewardPolicyLines;
}
