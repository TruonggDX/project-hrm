package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.AllowancePolicyApplicableTargetEntity;
import com.exo.hrm_project.entity.AllowancePolicyEntity;
import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import com.exo.hrm_project.mapper.AllowancePolicyLineMapper;
import com.exo.hrm_project.mapper.AllowancePolicyMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.AllowancePolicyApplicableTargetRepository;
import com.exo.hrm_project.repository.AllowancePolicyLineRepository;
import com.exo.hrm_project.repository.AllowancePolicyRepository;
import com.exo.hrm_project.service.IAllowancePolicyService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllowancePolicyServiceImpl implements IAllowancePolicyService {

  private final AllowancePolicyRepository allowancePolicyRepository;
  private final AllowancePolicyMapper allowancePolicyMapper;
  private final GenericIdMapper genericIdMapper;
  private final AllowancePolicyLineRepository allowancePolicyLineRepository;
  private final AllowancePolicyLineMapper allowancePolicyLineMapper;
  private final AllowancePolicyApplicableTargetRepository allowancePolicyApplicableTargetRepository;

  @Override
  public BaseResponse<ResponsePage<ListAllowancePolicyDto>> getAll(Pageable pageable,
      ListAllowancePolicyDto filter) {
    BaseResponse<ResponsePage<ListAllowancePolicyDto>> response = new BaseResponse<>();
    GenericSpecification<AllowancePolicyEntity> spec = new GenericSpecification<>();
    if (filter.getCode() != null && !filter.getCode().isEmpty()) {
      spec.add(new SearchCriteria("code", ":", filter.getCode()));
    }
    if (filter.getName() != null && !filter.getName().isEmpty()) {
      spec.add(new SearchCriteria("name", ":", filter.getName()));
    }
    if (filter.getApplicableType() != null) {
      spec.add(new SearchCriteria("applicableType", "=", filter.getApplicableType()));
    }
    if (filter.getType() != null) {
      spec.add(new SearchCriteria("type", "=", filter.getType()));
    }
    if (filter.getState() != null) {
      spec.add(new SearchCriteria("state", "=", filter.getState()));
    }
    if (filter.getStartDate() != null) {
      spec.add(new SearchCriteria("startDate", "=", filter.getStartDate()));
    }
    if (filter.getEndDate() != null) {
      spec.add(new SearchCriteria("endDate", "=", filter.getEndDate()));
    }
    Page<AllowancePolicyEntity> page = allowancePolicyRepository.findAll(spec, pageable);
    List<ListAllowancePolicyDto> dtos = page.getContent()
        .stream()
        .map(allowancePolicyMapper::toListDto)
        .toList();

    return ResponseUtils.toPageResponse(page, dtos, "Get All Allowance Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> createAllowancePolicy(
      AllowancePolicyDto allowancePolicyDto) {
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    AllowancePolicyEntity allowanceEntity = allowancePolicyMapper.toEntity(allowancePolicyDto);
    allowancePolicyRepository.save(allowanceEntity);
    Long policyId = allowanceEntity.getId();
    if (allowancePolicyDto.getTarget() != null) {
      List<AllowancePolicyApplicableTargetEntity> targetEntities = allowancePolicyDto.getTarget()
          .stream()
          .map(t -> {
            AllowancePolicyApplicableTargetEntity targetEntity = new AllowancePolicyApplicableTargetEntity();
            targetEntity.setAllowancePolicyId(policyId);
            targetEntity.setTargetId(t.getId());
            return targetEntity;
          }).toList();
      allowancePolicyApplicableTargetRepository.saveAll(targetEntities);
    }
    if (allowancePolicyDto.getAllowancePolicyLine() != null) {
      List<AllowancePolicyLineEntity> lineEntities = allowancePolicyDto.getAllowancePolicyLine()
          .stream()
          .map(lineDto -> {
            AllowancePolicyLineEntity lineEntity = allowancePolicyLineMapper.toEntity(lineDto);
            lineEntity.setAllowancePolicyId(policyId);
            return lineEntity;
          }).toList();
      allowancePolicyLineRepository.saveAll(lineEntities);
    }
    response.setCode(HttpStatus.CREATED.value());
    response.setMessage("Create Allowance Policy");
    response.setData(genericIdMapper.toResponseCommon(allowanceEntity));
    return response;
  }

  @Override
  public BaseResponse<ResponseCommon> updateAllowancePolicy(AllowancePolicyDto allowancePolicyDto) {
    return null;
  }

  @Override
  public BaseResponse<AllowancePolicyDto> getAllowancePolicy(Long id) {
    return null;
  }

  @Override
  public void deleteAllowancePolicy(Long id) {

  }
}
