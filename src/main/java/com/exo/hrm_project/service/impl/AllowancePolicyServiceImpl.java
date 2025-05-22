package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyLineDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.common.FilterRequest;
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
import com.exo.hrm_project.specification.filter.FilterPolicy;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
      FilterRequest filter) {
    GenericSpecification<AllowancePolicyEntity> spec = FilterPolicy.build(filter);
    Page<AllowancePolicyEntity> page = policyRepository.findAll(spec, pageable);
    return ResponseUtils.toPageResponse(page, page.getContent()
        .stream()
        .map(allowancePolicyMapper::toListDto)
        .toList(), "Get All Allowance Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> createAllowancePolicy(
      DetailAllowancePolicyDto allowancePolicyDto) {
    AllowancePolicyEntity entity = allowancePolicyMapper.toEntity(allowancePolicyDto);
    policyRepository.save(entity);
    Long policyId = entity.getId();
    createTargets(allowancePolicyDto, policyId);
    createPolicyLines(allowancePolicyDto, policyId);
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity),
        "Create Allowance Policy");
  }

  private void createTargets(DetailAllowancePolicyDto dto, Long policyId) {
    if (dto.getApplicableTargets() == null || dto.getApplicableTargets().isEmpty()) {
      return;
    }
    List<AllowancePolicyApplicableTargetEntity> targets = dto.getApplicableTargets().stream()
        .map(t -> {
          AllowancePolicyApplicableTargetEntity entity = new AllowancePolicyApplicableTargetEntity();
          entity.setAllowancePolicyId(policyId);
          entity.setTargetId(t.getId());
          return entity;
        }).toList();
    applicableTargetRepository.saveAll(targets);
  }

  private void createPolicyLines(DetailAllowancePolicyDto dto, Long policyId) {
    if (dto.getAllowancePolicyLines() == null || dto.getAllowancePolicyLines().isEmpty()) {
      return;
    }
    List<AllowancePolicyLineEntity> lines = dto.getAllowancePolicyLines().stream()
        .map(lineDto -> {
          AllowancePolicyLineEntity entity = allowancePolicyLineMapper.toEntity(lineDto);
          entity.setAllowancePolicyId(policyId);
          return entity;
        }).toList();
    policyLineRepository.saveAll(lines);
  }

  @Override
  public BaseResponse<ResponseCommon> updateAllowancePolicy(
      DetailAllowancePolicyDto allowancePolicyDto) {
    AllowancePolicyEntity entity = getAllowancePolicyById(allowancePolicyDto.getId());
    allowancePolicyMapper.updateAllowancePolicy(allowancePolicyDto, entity);
    policyRepository.save(entity);
    updateApplicableTargets(allowancePolicyDto);
    updatePolicyLines(allowancePolicyDto);
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity),
        "Update Allowance Policy");
  }

  private void updateApplicableTargets(DetailAllowancePolicyDto dto) {
    List<AllowancePolicyApplicableTargetEntity> existingTargets =
        applicableTargetRepository.findByAllowancePolicyId(dto.getId());
    applicableTargetRepository.deleteAll(existingTargets);
    createTargets(dto, dto.getId());
  }

  private void updatePolicyLines(DetailAllowancePolicyDto dto) {
    List<AllowancePolicyLineEntity> lineEntities = dto.getAllowancePolicyLines().stream()
        .map(lineDto -> {
          AllowancePolicyLineEntity lineEntity;
          if (lineDto.getId() != null) {
            lineEntity = policyLineRepository.findById(lineDto.getId())
                .orElseThrow(() -> new NotFoundException("Line not found id " + lineDto.getId()));
            allowancePolicyLineMapper.updatePolicyLine(lineDto, lineEntity);
          } else {
            lineEntity = allowancePolicyLineMapper.toEntity(lineDto);
          }
          lineEntity.setAllowancePolicyId(dto.getId());
          return lineEntity;
        }).toList();
    policyLineRepository.saveAll(lineEntities);
  }


  @Override
  public BaseResponse<DetailAllowancePolicyDto> getAllowancePolicy(Long id) {
    AllowancePolicyEntity policyEntity = getAllowancePolicyById(id);
    DetailAllowancePolicyDto allowancePolicyDto = allowancePolicyMapper.toDto(policyEntity);
    List<AllowancePolicyLineEntity> lineEntities = policyLineRepository.findByAllowancePolicyId(id);
    List<DetailAllowancePolicyLineDto> lineDtos = lineEntities.stream().map(line -> {
      DetailAllowancePolicyLineDto lineDto = allowancePolicyLineMapper.toDto(line);
      AllowanceEntity allowance = allowanceRepository.findById(line.getAllowanceId()).orElseThrow(
          () -> new NotFoundException("Not found allowance id :" + line.getAllowanceId()));
      lineDto.setAllowance(allowanceMapper.toDetailDto(allowance));
      return lineDto;
    }).toList();
    allowancePolicyDto.setAllowancePolicyLines(lineDtos);
    String type = allowancePolicyDto.getApplicableType();
    if (type != null && !type.isBlank() && !type.equalsIgnoreCase("ALL")) {
      List<CommonDto> applicableTargets = fetchApplicableTargetsByType(id, type);
      allowancePolicyDto.setApplicableTargets(applicableTargets);
    }
    return BaseResponse.success(allowancePolicyDto, "Get Allowance Policy");
  }

  private List<CommonDto> fetchApplicableTargetsByType(Long policyId, String applicableType) {
    List<AllowancePolicyApplicableTargetEntity> targets =
        applicableTargetRepository.findByAllowancePolicyId(policyId);
    List<Long> ids = targets.stream()
        .map(AllowancePolicyApplicableTargetEntity::getTargetId)
        .toList();
    return switch (applicableType.toUpperCase()) {
      case "EMPLOYEE" -> iExternalService.getEmployeeInfoByIds(ids);
      case "DEPARTMENT" -> iExternalService.getDepartmentInfoByIds(ids);
      case "POSITION" -> iExternalService.getPositionInfoByIds(ids);
      default -> List.of();
    };
  }

  @Override
  public void deleteAllowancePolicy(Long id) {
    AllowancePolicyEntity allowancePolicyEntity = getAllowancePolicyById(id);
    List<AllowancePolicyLineEntity> policyLineEntity = policyLineRepository.findByAllowancePolicyId(
        id);
    policyLineRepository.deleteAll(policyLineEntity);
    List<AllowancePolicyApplicableTargetEntity> applicableTargetEntities = applicableTargetRepository.findByAllowancePolicyId(
        id);
    applicableTargetRepository.deleteAll(applicableTargetEntities);
    policyRepository.delete(allowancePolicyEntity);
  }

  private AllowancePolicyEntity getAllowancePolicyById(Long id) {
    return policyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance policy id : " + id));
  }
}
