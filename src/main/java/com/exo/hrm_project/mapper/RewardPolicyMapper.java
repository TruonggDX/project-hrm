package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.entity.RewardPolicyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RewardPolicyMapper {

  ListRewardPolicyDto toListDto(RewardPolicyEntity entity);

  DetailRewardPolicyDto toDto(RewardPolicyEntity entity);

  RewardPolicyEntity toEntity(DetailRewardPolicyDto dto);

  void updateRewardPolicy(DetailRewardPolicyDto dto, @MappingTarget RewardPolicyEntity entity);

}
