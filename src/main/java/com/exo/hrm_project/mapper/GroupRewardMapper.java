package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.group_allowance_reward.GroupRewardDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupRewardDto;
import com.exo.hrm_project.entity.GroupRewardEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupRewardMapper {

  GroupRewardDto toDto(GroupRewardEntity entity);

  GroupRewardEntity toEntity(GroupRewardDto dto);

  ListGroupRewardDto toDtoList(GroupRewardEntity entity);

  CommonDto toCommonDto(GroupRewardEntity entity);

  void updateGroupReward(GroupRewardDto dto, @MappingTarget GroupRewardEntity target);

}
