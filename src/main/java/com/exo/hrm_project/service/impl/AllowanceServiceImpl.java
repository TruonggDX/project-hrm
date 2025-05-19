package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance.ListAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.AllowanceMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.AllowanceRepository;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.service.IAllowanceService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AllowanceServiceImpl implements IAllowanceService {

  private final AllowanceRepository allowanceRepository;
  private final AllowanceMapper allowanceMapper;
  private final GroupAllowanceRepository groupAllowanceRepository;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListAllowanceDto>> getAll(Pageable pageable, String code,
      String name, Boolean isActive) {
    GenericSpecification<AllowanceEntity> spec = new GenericSpecification<>();
    if (code != null && !code.isEmpty()) {
      spec.add(new SearchCriteria("code", ":", code));
    }
    if (name != null && !name.isEmpty()) {
      spec.add(new SearchCriteria("name", ":", name));
    }
    if (isActive != null) {
      spec.add(new SearchCriteria("isActive", "=", isActive));
    }
    Page<AllowanceEntity> page = allowanceRepository.findAll(spec, pageable);
    List<ListAllowanceDto> allowanceDtos = page.getContent().stream()
        .map(allowanceMapper::toListDto)
        .toList();
    return ResponseUtils.toPageResponse(page, allowanceDtos, "Get All Allowance");
  }

  @Override
  public BaseResponse<ResponseCommon> createAllowance(AllowanceDto allowanceDto) {
    Optional<GroupAllowanceEntity> check = groupAllowanceRepository.findById(
        allowanceDto.getGroupAllowance().getId());
    if (check.isEmpty()) {
      return BaseResponse.notFound(
          "Not found group allowance id: " + allowanceDto.getGroupAllowance().getId());
    }
    AllowanceEntity allowanceEntity = allowanceMapper.toEntity(allowanceDto);
    if (allowanceDto.getGroupAllowance() != null
        && allowanceDto.getGroupAllowance().getId() != null) {
      allowanceEntity.setGroupAllowanceId(allowanceDto.getGroupAllowance().getId());
    }
    if (allowanceDto.getUom() != null && allowanceDto.getUom().getId() != null) {
      allowanceEntity.setUomId(allowanceDto.getUom().getId());
    }
    if (allowanceDto.getCurrency() != null && allowanceDto.getCurrency().getId() != null) {
      allowanceEntity.setCurrencyId(allowanceDto.getCurrency().getId());
    }
    allowanceRepository.save(allowanceEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(allowanceEntity),
        "Create Allowance successfully");
  }

  @Override
  public BaseResponse<ResponseCommon> updateAllowance(AllowanceDto allowanceDto) {
    Optional<AllowanceEntity> checkAllowanceId = allowanceRepository.findById(allowanceDto.getId());
    if (checkAllowanceId.isEmpty()) {
      return BaseResponse.notFound("Not found allowance id: " + allowanceDto.getId());
    }
    Optional<GroupAllowanceEntity> check = groupAllowanceRepository.findById(
        allowanceDto.getGroupAllowance().getId());
    if (check.isEmpty()) {
      return BaseResponse.notFound(
          "Not found group allowance id: " + allowanceDto.getGroupAllowance().getId());
    }
    AllowanceEntity allowanceEntity = checkAllowanceId.get();
    allowanceMapper.updateDto(allowanceDto, allowanceEntity);
    if (allowanceDto.getGroupAllowance() != null
        && allowanceDto.getGroupAllowance().getId() != null) {
      allowanceEntity.setGroupAllowanceId(allowanceDto.getGroupAllowance().getId());
    }
    if (allowanceDto.getUom() != null && allowanceDto.getUom().getId() != null) {
      allowanceEntity.setUomId(allowanceDto.getUom().getId());
    }
    if (allowanceDto.getCurrency() != null && allowanceDto.getCurrency().getId() != null) {
      allowanceEntity.setCurrencyId(allowanceDto.getCurrency().getId());
    }
    allowanceRepository.save(allowanceEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(allowanceEntity),
        "Update Allowance successfully");
  }

  @Override
  public BaseResponse<AllowanceDto> getAllowanceById(Long id) {
    AllowanceEntity allowanceEntity = allowanceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance id : " + id));
    AllowanceDto dto = allowanceMapper.toDto(allowanceEntity);
    return BaseResponse.success(dto, "Get Allowance successfully");
  }

  @Override
  public void deleteAllowance(Long id) {
    AllowanceEntity allowanceEntity = allowanceRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance id : " + id));
    allowanceRepository.delete(allowanceEntity);
  }
}
