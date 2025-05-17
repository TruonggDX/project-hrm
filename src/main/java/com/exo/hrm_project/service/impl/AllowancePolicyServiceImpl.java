package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyLineDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowanceDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyLineDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.AllowancePolicyApplicableTargetEntity;
import com.exo.hrm_project.entity.AllowancePolicyEntity;
import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.AllowanceMapper;
import com.exo.hrm_project.mapper.AllowancePolicyLineMapper;
import com.exo.hrm_project.mapper.AllowancePolicyMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.AllowancePolicyApplicableTargetRepository;
import com.exo.hrm_project.repository.AllowancePolicyLineRepository;
import com.exo.hrm_project.repository.AllowancePolicyRepository;
import com.exo.hrm_project.repository.AllowanceRepository;
import com.exo.hrm_project.service.IAllowancePolicyService;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllowancePolicyServiceImpl implements IAllowancePolicyService {

  private final AllowancePolicyRepository policyRepository;
  private final AllowancePolicyMapper allowancePolicyMapper;
  private final GenericIdMapper genericIdMapper;
  private final AllowancePolicyLineRepository policyLineRepository;
  private final AllowancePolicyLineMapper allowancePolicyLineMapper;
  private final AllowancePolicyApplicableTargetRepository applicableTargetRepository;
  private final AllowanceRepository allowanceRepository;
  private final AllowanceMapper allowanceMapper;
  private final IExternalService iExternalService;

  @Override
  public BaseResponse<ResponsePage<ListAllowancePolicyDto>> getAll(Pageable pageable,
      ListAllowancePolicyDto filter) {
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
    Page<AllowancePolicyEntity> page = policyRepository.findAll(spec, pageable);
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
    AllowancePolicyEntity entity = savePolicy(allowancePolicyDto);
    Long policyId = entity.getId();
    saveTargets(allowancePolicyDto, policyId);
    savePolicyLines(allowancePolicyDto, policyId);
    response.setCode(HttpStatus.CREATED.value());
    response.setMessage("Create Allowance Policy");
    response.setData(genericIdMapper.toResponseCommon(entity));
    return response;
  }

  private AllowancePolicyEntity savePolicy(AllowancePolicyDto dto) {
    AllowancePolicyEntity entity = allowancePolicyMapper.toEntity(dto);
    return policyRepository.save(entity);
  }

  private void saveTargets(AllowancePolicyDto dto, Long policyId) {
    if (dto.getTarget() == null || dto.getTarget().isEmpty()) {
      return;
    }
    List<AllowancePolicyApplicableTargetEntity> targets = dto.getTarget().stream()
        .map(t -> {
          AllowancePolicyApplicableTargetEntity entity = new AllowancePolicyApplicableTargetEntity();
          entity.setAllowancePolicyId(policyId);
          entity.setTargetId(t.getId());
          return entity;
        }).toList();
    applicableTargetRepository.saveAll(targets);
  }

  private void savePolicyLines(AllowancePolicyDto dto, Long policyId) {
    if (dto.getAllowancePolicyLine() == null || dto.getAllowancePolicyLine().isEmpty()) {
      return;
    }
    List<AllowancePolicyLineEntity> lines = dto.getAllowancePolicyLine().stream()
        .map(lineDto -> {
          AllowancePolicyLineEntity entity = allowancePolicyLineMapper.toEntity(lineDto);
          entity.setAllowancePolicyId(policyId);
          return entity;
        }).toList();
    policyLineRepository.saveAll(lines);
  }

  @Override
  public BaseResponse<ResponseCommon> updateAllowancePolicy(AllowancePolicyDto allowancePolicyDto) {
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    Optional<AllowancePolicyEntity> check = policyRepository.findById(allowancePolicyDto.getId());
    if (check.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage("Policy not found id " + allowancePolicyDto.getId());
      return response;
    }
    AllowancePolicyEntity entity = check.get();
    allowancePolicyMapper.updateAllowancePolicy(allowancePolicyDto, entity);
    policyRepository.save(entity);
    List<AllowancePolicyApplicableTargetEntity> applicableTargetEntities = applicableTargetRepository.findByAllowancePolicyId(
        allowancePolicyDto.getId());
    applicableTargetRepository.deleteAll(applicableTargetEntities);
    saveTargets(allowancePolicyDto, allowancePolicyDto.getId());
    List<AllowancePolicyLineEntity> lineEntities = new ArrayList<>();
    for (AllowancePolicyLineDto lineDto : allowancePolicyDto.getAllowancePolicyLine()) {
      Optional<AllowancePolicyLineEntity> checkPolicyLine = policyLineRepository.findById(
          lineDto.getId());
      if (checkPolicyLine.isEmpty()) {
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage("Line not found id " + lineDto.getId());
        return response;
      }
      AllowancePolicyLineEntity lineEntity = checkPolicyLine.get();
      allowancePolicyLineMapper.updatePolicyLine(lineDto, lineEntity);
      lineEntity.setAllowancePolicyId(allowancePolicyDto.getId());
      lineEntities.add(lineEntity);
    }
    policyLineRepository.saveAll(lineEntities);
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Update Allowance Policy");
    response.setData(genericIdMapper.toResponseCommon(entity));
    return response;
  }

  @Override
  public BaseResponse<DetailAllowancePolicyDto> getAllowancePolicy(Long id) {
    BaseResponse<DetailAllowancePolicyDto> response = new BaseResponse<>();
    AllowancePolicyEntity policyEntity = policyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance policy id :" + id));
    DetailAllowancePolicyDto allowancePolicyDto = allowancePolicyMapper.toDto(policyEntity);
    List<AllowancePolicyLineEntity> lineEntities = policyLineRepository.findByAllowancePolicyId(id);
    List<DetailAllowancePolicyLineDto> lineDtos = lineEntities.stream().map(line -> {
      DetailAllowancePolicyLineDto lineDto = allowancePolicyLineMapper.toDto(line);
      AllowanceEntity allowance = allowanceRepository.findById(line.getAllowanceId()).orElseThrow(
          () -> new NotFoundException("Not found allowance id :" + line.getAllowanceId()));
      if (allowance != null) {
        DetailAllowanceDto allowanceDto = allowanceMapper.toDetailDto(allowance);
        lineDto.setAllowance(allowanceDto);
      }
      return lineDto;
    }).toList();
    allowancePolicyDto.setAllowancePolicyLine(lineDtos);
    String type = allowancePolicyDto.getApplicableType();
    if (type != null && !type.isBlank()) {
      List<CommonDto> applicableTargets = fetchApplicableTargets(id, type);
      allowancePolicyDto.setApplicableTarget(applicableTargets);
    }
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Get Allowance Policy");
    response.setData(allowancePolicyDto);
    return response;
  }

  private List<CommonDto> fetchApplicableTargets(Long policyId, String applicableType) {
    List<AllowancePolicyApplicableTargetEntity> targets =
        applicableTargetRepository.findByAllowancePolicyId(policyId);
    List<Long> ids = targets.stream()
        .map(AllowancePolicyApplicableTargetEntity::getTargetId)
        .toList();
    return switch (applicableType.toUpperCase()) {
      case "EMPLOYEE" -> iExternalService.getEmployeeInfoByIds(ids);
      case "DEPARTMENT" -> iExternalService.getDepartmentInfoByIds(ids);
      case "POSITION" -> iExternalService.getPositionInfoByIds(ids);
      case "ALL" -> List.of();
      default -> List.of();
    };
  }

  @Override
  public void deleteAllowancePolicy(Long id) {
    AllowancePolicyEntity allowancePolicyEntity = policyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance policy id : " + id));
    Long policyId = allowancePolicyEntity.getId();
    List<AllowancePolicyLineEntity> policyLineEntity = policyLineRepository.findByAllowancePolicyId(
        policyId);
    policyLineRepository.deleteAll(policyLineEntity);
    List<AllowancePolicyApplicableTargetEntity> applicableTargetEntities = applicableTargetRepository.findByAllowancePolicyId(
        policyId);
    applicableTargetRepository.deleteAll(applicableTargetEntities);
    policyRepository.delete(allowancePolicyEntity);
  }
}
