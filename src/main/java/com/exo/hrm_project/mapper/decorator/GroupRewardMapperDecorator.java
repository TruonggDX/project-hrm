package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.group_allowance_reward.GroupRewardDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupRewardDto;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.mapper.GroupRewardMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class GroupRewardMapperDecorator implements GroupRewardMapper {

  private final GroupRewardMapper delegate;
  private final GroupRewardRepository repo;

  @Override
  public GroupRewardDto toDto(GroupRewardEntity entity) {
    GroupRewardDto dto = delegate.toDto(entity);
    if (entity.getParentId() != null) {
      repo.findById(entity.getParentId())
          .ifPresent(parent -> dto.setParent(delegate.toCommonDto(parent)));
    }
    return dto;
  }

  @Override
  public GroupRewardEntity toEntity(GroupRewardDto dto) {
    return delegate.toEntity(dto);
  }

  @Override
  public ListGroupRewardDto toDtoList(GroupRewardEntity entity) {
    return delegate.toDtoList(entity);
  }

  @Override
  public CommonDto toCommonDto(GroupRewardEntity entity) {
    return delegate.toCommonDto(entity);
  }

  @Override
  public void updateGroupReward(GroupRewardDto dto, GroupRewardEntity target) {
    delegate.updateGroupReward(dto, target);
  }
}
