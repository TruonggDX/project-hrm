package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance.ListAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.AllowanceMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.AllowanceRepository;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.service.IAllowanceService;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.filter.FilterGroupAllowance;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AllowanceServiceImpl implements IAllowanceService {

  private final AllowanceRepository allowanceRepository;
  private final AllowanceMapper allowanceMapper;
  private final GroupAllowanceRepository groupAllowanceRepository;
  private final GenericIdMapper genericIdMapper;
  private final IExternalService iExternalService;

  @Override
  public BaseResponse<ResponsePage<ListAllowanceDto>> getAll(Pageable pageable, String code,
      String name, Boolean isActive) {
    GenericSpecification<AllowanceEntity> spec = FilterGroupAllowance.build(code, name, isActive);
    Page<AllowanceEntity> page = allowanceRepository.findAll(spec, pageable);
    List<ListAllowanceDto> allowanceDtos = page.getContent().stream()
        .map(entity -> {
          ListAllowanceDto dto = allowanceMapper.toListDto(entity);
          setGroupAllowanceDto(entity, dto);
          return dto;
        }).toList();
    return ResponseUtils.toPageResponse(page, allowanceDtos, "Get All Allowance");
  }

  @Override
  public BaseResponse<ResponseCommon> createAllowance(AllowanceDto allowanceDto) {
    AllowanceEntity allowanceEntity = allowanceMapper.toEntity(allowanceDto);
    allowanceRepository.save(allowanceEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(allowanceEntity),
        "Create Allowance successfully");
  }

  @Override
  public BaseResponse<ResponseCommon> updateAllowance(AllowanceDto allowanceDto) {
    AllowanceEntity allowanceEntity = getAllowanceEntityById(allowanceDto.getId());
    allowanceMapper.updateDto(allowanceDto, allowanceEntity);
    allowanceRepository.save(allowanceEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(allowanceEntity),
        "Update Allowance successfully");
  }


  @Override
  public BaseResponse<AllowanceDto> getAllowanceById(Long id) {
    AllowanceEntity allowanceEntity = getAllowanceEntityById(id);
    return BaseResponse.success(toDto(allowanceEntity), "Get Allowance successfully");
  }

  @Override
  public void deleteAllowance(Long id) {
    AllowanceEntity allowanceEntity = getAllowanceEntityById(id);
    allowanceRepository.delete(allowanceEntity);
  }

  private AllowanceEntity getAllowanceEntityById(Long id) {
    return allowanceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance id : " + id));
  }

  public AllowanceDto toDto(AllowanceEntity entity) {
    AllowanceDto dto = allowanceMapper.toDto(entity);
    if (entity.getGroupAllowanceId() != null) {
      groupAllowanceRepository.findById(entity.getGroupAllowanceId())
          .ifPresent(parent -> dto.setGroupAllowance(allowanceMapper.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;
  }

  private void setGroupAllowanceDto(AllowanceEntity entity, ListAllowanceDto dto) {
    if (entity.getGroupAllowanceId() != null) {
      groupAllowanceRepository.findById(entity.getGroupAllowanceId())
          .ifPresent(parent -> dto.setGroupAllowance(allowanceMapper.toCommonDto(parent)));
    }
  }

}
