package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.group_allowance_reward.GroupAllowanceDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupAllowanceDto;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface GroupAllowanceMapper {

  GroupAllowanceDto toDto(GroupAllowanceEntity entity);

  ListGroupAllowanceDto toDtoList(GroupAllowanceEntity entity);

  @Mapping(target = "parentId", source = "parent.id")
  GroupAllowanceEntity toEntity(GroupAllowanceDto dto);

  CommonDto toCommonDto(GroupAllowanceEntity entity);

  @Mapping(target = "parentId", source = "parent.id")
  void updateGroupAllowance(GroupAllowanceDto dto, @MappingTarget GroupAllowanceEntity entity);
}
