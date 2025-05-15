package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.group_allowance_reward.GroupAllowanceDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupAllowanceDto;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.mapper.GroupAllowanceMapper;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.MappingTarget;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class GroupAllowanceMapperDecorator implements GroupAllowanceMapper {

  private final GroupAllowanceMapper delegate;
  private final GroupAllowanceRepository repo;


  @Override
  public GroupAllowanceDto toDto(GroupAllowanceEntity entity) {
    GroupAllowanceDto dto = delegate.toDto(entity);
    if (entity.getParentId() != null) {
      repo.findById(entity.getParentId())
          .ifPresent(parent -> dto.setParent(delegate.toCommonDto(parent)));
    }
    return dto;
  }

  @Override
  public ListGroupAllowanceDto toDtoList(GroupAllowanceEntity entity) {
    return delegate.toDtoList(entity);
  }

  @Override
  public GroupAllowanceEntity toEntity(GroupAllowanceDto dto) {
    return delegate.toEntity(dto);
  }

  @Override
  public CommonDto toCommonDto(GroupAllowanceEntity entity) {
    return delegate.toCommonDto(entity);
  }

  @Override
  public void updateGroupAllowance(GroupAllowanceDto dto, @MappingTarget GroupAllowanceEntity entity) {
    delegate.updateGroupAllowance(dto, entity);
  }
}
