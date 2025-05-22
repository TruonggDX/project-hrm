package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.allowance_policy.DetailAllowanceDto;
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
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.service.IAllowancePolicyService;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.filter.FilterPolicy;
import com.exo.hrm_project.utils.enums.ApplicableObject;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
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
  private final GroupAllowanceRepository groupAllowanceRepository;

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

  @Transactional
  @Override
  public BaseResponse<ResponseCommon> createAllowancePolicy(
      DetailAllowancePolicyDto allowancePolicyDto) {
    AllowancePolicyEntity entity = allowancePolicyMapper.toEntity(allowancePolicyDto);
    policyRepository.save(entity);
    Long policyId = entity.getId();
    createOrUpdateApplicableTargets(policyId, allowancePolicyDto.getApplicableTargets());
    createOrUpdatePolicyLines(policyId, allowancePolicyDto.getAllowancePolicyLines());
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity),
        "Create Allowance Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> updateAllowancePolicy(
      DetailAllowancePolicyDto allowancePolicyDto) {
    AllowancePolicyEntity entity = getAllowancePolicyById(allowancePolicyDto.getId());
    allowancePolicyMapper.updateAllowancePolicy(allowancePolicyDto, entity);
    policyRepository.save(entity);
    createOrUpdateApplicableTargets(entity.getId(), allowancePolicyDto.getApplicableTargets());
    createOrUpdatePolicyLines(entity.getId(), allowancePolicyDto.getAllowancePolicyLines());
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity),
        "Update Allowance Policy");
  }

  private void createOrUpdateApplicableTargets(Long policyId, List<CommonDto> applicableTargets) {
    List<AllowancePolicyApplicableTargetEntity> existingTargets =
        applicableTargetRepository.findByAllowancePolicyId(policyId);
    applicableTargetRepository.deleteAll(existingTargets);

    if (applicableTargets == null || applicableTargets.isEmpty()) {
      return;
    }
    List<AllowancePolicyApplicableTargetEntity> newTargets = applicableTargets.stream()
        .map(t -> {
          AllowancePolicyApplicableTargetEntity entity = new AllowancePolicyApplicableTargetEntity();
          entity.setAllowancePolicyId(policyId);
          entity.setTargetId(t.getId());
          return entity;
        }).toList();

    applicableTargetRepository.saveAll(newTargets);
  }

  private void createOrUpdatePolicyLines(Long policyId,
      List<DetailAllowancePolicyLineDto> lineDtos) {
    List<AllowancePolicyLineEntity> existingLines = policyLineRepository.findByAllowancePolicyId(
        policyId);
    Map<Long, AllowancePolicyLineEntity> existingLineMap = existingLines.stream()
        .filter(line -> line.getId() != null)
        .collect(Collectors.toMap(AllowancePolicyLineEntity::getId, Function.identity()));
    List<AllowancePolicyLineEntity> linesToSave = new ArrayList<>();
    for (DetailAllowancePolicyLineDto lineDto : lineDtos) {
      AllowancePolicyLineEntity lineEntity;
      if (lineDto.getId() != null && existingLineMap.containsKey(lineDto.getId())) {
        lineEntity = existingLineMap.get(lineDto.getId());
        allowancePolicyLineMapper.updatePolicyLine(lineDto, lineEntity);
        existingLineMap.remove(lineDto.getId());
      } else {
        lineEntity = allowancePolicyLineMapper.toEntity(lineDto);
        lineEntity.setAllowancePolicyId(policyId);
      }
      linesToSave.add(lineEntity);
    }
    if (!existingLineMap.isEmpty()) {
      policyLineRepository.deleteAll(existingLineMap.values());
    }
    policyLineRepository.saveAll(linesToSave);
  }

  @Override
  public BaseResponse<DetailAllowancePolicyDto> getAllowancePolicy(Long id) {
    AllowancePolicyEntity policyEntity = getAllowancePolicyById(id);
    DetailAllowancePolicyDto allowancePolicyDto = allowancePolicyMapper.toDto(policyEntity);
    List<AllowancePolicyLineEntity> lineEntities = policyLineRepository.findByAllowancePolicyId(id);

    Map<Long, DetailAllowanceDto> allowanceDtoMap = getAllowanceDtoMap(lineEntities);
    List<DetailAllowancePolicyLineDto> lineDtos = buildAllowancePolicyLineDtos(lineEntities,
        allowanceDtoMap);
    allowancePolicyDto.setAllowancePolicyLines(lineDtos);
    ApplicableObject applicableType = policyEntity.getApplicableType();
    List<CommonDto> applicableTargets = getApplicableTargets(id, applicableType);
    allowancePolicyDto.setApplicableTargets(applicableTargets);
    return BaseResponse.success(allowancePolicyDto, "Get Allowance Policy");
  }

  private Map<Long, DetailAllowanceDto> getAllowanceDtoMap(List<AllowancePolicyLineEntity> lines) {
    Set<Long> allowanceIds = lines.stream().map(AllowancePolicyLineEntity::getAllowanceId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    List<AllowanceEntity> allowanceEntities = allowanceRepository.findAllById(allowanceIds);
    return allowanceEntities.stream().map(this::getDataExternal).collect(Collectors.toMap(
        DetailAllowanceDto::getId,
        Function.identity()
    ));
  }

  private List<DetailAllowancePolicyLineDto> buildAllowancePolicyLineDtos(
      List<AllowancePolicyLineEntity> lines, Map<Long, DetailAllowanceDto> allowanceDtoMap) {
    return lines.stream().map(line -> {
      DetailAllowancePolicyLineDto lineDto = allowancePolicyLineMapper.toDto(line);
      lineDto.setAllowance(allowanceDtoMap.get(line.getAllowanceId()));
      return lineDto;
    }).toList();
  }

  public DetailAllowanceDto getDataExternal(AllowanceEntity entity) {
    DetailAllowanceDto dto = allowanceMapper.toDetailDto(entity);
    if (entity.getGroupAllowanceId() != null) {
      groupAllowanceRepository.findById(entity.getGroupAllowanceId())
          .ifPresent(parent -> dto.setGroupAllowance(allowanceMapper.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;
  }

  private List<CommonDto> getApplicableTargets(Long policyId, ApplicableObject applicableType) {
    if (applicableType == null || applicableType == ApplicableObject.All) {
      return List.of();
    }
    List<AllowancePolicyApplicableTargetEntity> targets =
        applicableTargetRepository.findByAllowancePolicyId(policyId);
    List<Long> ids = targets.stream()
        .map(AllowancePolicyApplicableTargetEntity::getTargetId)
        .toList();
    return switch (applicableType) {
      case EMPLOYEE -> iExternalService.getEmployeeInfoByIds(ids);
      case DEPARTMENT -> iExternalService.getDepartmentInfoByIds(ids);
      case POSITION -> iExternalService.getPositionInfoByIds(ids);
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
