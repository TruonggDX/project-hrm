package com.exo.hrm_project.mapper;


import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyLineDto;
import com.exo.hrm_project.entity.RewardPolicyLineEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RewardPolicyLineMapper {

  DetailRewardPolicyLineDto toDto(RewardPolicyLineEntity rewardPolicyLineEntity);


}
