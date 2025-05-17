package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance.ListAllowanceDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowanceDto;
import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.mapper.AllowanceMapper;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.service.IExternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class AllowanceMapperDecorator implements AllowanceMapper {

  private final AllowanceMapper delegate;
  private final GroupAllowanceRepository repo;
  private final IExternalService iExternalService;

  @Override
  public AllowanceDto toDto(AllowanceEntity entity) {
    AllowanceDto dto = delegate.toDto(entity);
    if (entity.getGroupAllowanceId() != null) {
      repo.findById(entity.getGroupAllowanceId())
          .ifPresent(parent -> dto.setGroupAllowance(delegate.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;
  }


  @Override
  public ListAllowanceDto toListDto(AllowanceEntity entity) {
    ListAllowanceDto dto = delegate.toListDto(entity);
    if (entity.getGroupAllowanceId() != null) {
      repo.findById(entity.getGroupAllowanceId())
          .ifPresent(parent -> dto.setGroupAllowance(delegate.toCommonDto(parent)));
    }
    return dto;
  }

  @Override
  public CommonDto toCommonDto(GroupAllowanceEntity entity) {
    return delegate.toCommonDto(entity);
  }

  @Override
  public AllowanceEntity toEntity(AllowanceDto dto) {
    return delegate.toEntity(dto);
  }

  @Override
  public DetailAllowanceDto toDetailDto(AllowanceEntity entity) {
    DetailAllowanceDto dto = delegate.toDetailDto(entity);
    if (entity.getGroupAllowanceId() != null) {
      repo.findById(entity.getGroupAllowanceId())
          .ifPresent(parent -> dto.setGroupAllowance(delegate.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;
  }

  @Override
  public void updateDto(AllowanceDto dto, AllowanceEntity entity) {
    delegate.updateDto(dto, entity);
  }
}
