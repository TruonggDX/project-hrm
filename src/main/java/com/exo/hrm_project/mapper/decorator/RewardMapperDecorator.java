package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardDto;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.service.IExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class RewardMapperDecorator implements RewardMapper {

  private final RewardMapper delegate;
  private final GroupRewardRepository repo;
  private final IExternalService iExternalService;

  @Override
  public RewardDto toDto(RewardEntity entity) {
    RewardDto dto = delegate.toDto(entity);
    if (entity.getGroupRewardId() != null) {
      repo.findById(entity.getGroupRewardId())
          .ifPresent(parent -> dto.setGroupReward(delegate.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
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
  public RewardEntity toEntity(RewardDto dto) {
    return delegate.toEntity(dto);
  }

  @Override
  public void updateDto(RewardDto dto, RewardEntity entity) {
    delegate.updateDto(dto, entity);
  }

  @Override
  public DetailRewardDto toDetailDto(RewardEntity entity) {
    DetailRewardDto dto = delegate.toDetailDto(entity);
    if (entity.getGroupRewardId() != null) {
      repo.findById(entity.getGroupRewardId())
          .ifPresent(parent -> dto.setGroupReward(delegate.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;  }
}
