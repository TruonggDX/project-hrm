package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class RewardMapperDecorator implements RewardMapper {

  private final RewardMapper delegate;
  private final GroupRewardRepository repo;

  @Override
  public RewardDto toDto(RewardEntity entity) {
    RewardDto dto = delegate.toDto(entity);
    if (entity.getGroupRewardId() != null) {
      repo.findById(entity.getGroupRewardId())
          .ifPresent(parent -> dto.setGroupReward(delegate.toCommonDto(parent)));
    }
    return dto;
  }

  @Override
  public ListRewardDto toListDto(RewardEntity entity) {
    return delegate.toListDto(entity);
  }

  @Override
  public CommonDto toCommonDto(GroupRewardEntity entity) {
    return delegate.toCommonDto(entity);
  }

  @Override
  public RewardDto toAddDto(RewardEntity entity) {
    RewardDto dto = delegate.toAddDto(entity);
    if (entity.getGroupRewardId() != null) {
      repo.findById(entity.getGroupRewardId())
          .ifPresent(parent -> dto.setGroupReward(delegate.toCommonDto(parent)));
    }
    return dto;
  }

  @Override
  public RewardEntity toEntity(RewardDto dto) {
    return delegate.toEntity(dto);
  }

  @Override
  public void updateDto(RewardDto dto, RewardEntity entity) {
    delegate.updateDto(dto, entity);
  }
}
