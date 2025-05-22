package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyLineDto;
import com.exo.hrm_project.entity.RewardPolicyLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RewardPolicyLineMapper {

  @Mapping(source = "reward.id", target = "rewardId")
  RewardPolicyLineEntity toEntity(DetailRewardPolicyLineDto rewardPolicyLineDto);

  DetailRewardPolicyLineDto toDto(RewardPolicyLineEntity rewardPolicyLineEntity);

  void updatePolicyLine(DetailRewardPolicyLineDto rewardPolicyLineDto,
      @MappingTarget RewardPolicyLineEntity rewardPolicyLineEntity);
}
