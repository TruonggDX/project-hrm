package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.common.FilterRequest;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward_policy.DetailRewardDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyLineDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.entity.RewardPolicyApplicableTargetEntity;
import com.exo.hrm_project.entity.RewardPolicyEntity;
import com.exo.hrm_project.entity.RewardPolicyLineEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.mapper.RewardPolicyLineMapper;
import com.exo.hrm_project.mapper.RewardPolicyMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.repository.RewardPolicyApplicableTargetRepository;
import com.exo.hrm_project.repository.RewardPolicyLineRepository;
import com.exo.hrm_project.repository.RewardPolicyRepository;
import com.exo.hrm_project.repository.RewardRepository;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.service.IRewardPolicyService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.filter.FilterPolicy;
import com.exo.hrm_project.utils.enums.ApplicableObject;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
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
public class RewardPolicyServiceImpl implements IRewardPolicyService {

  private final RewardPolicyRepository rewardPolicyRepository;
  private final RewardPolicyMapper rewardPolicyMapper;
  private final RewardPolicyLineMapper rewardPolicyLineMapper;
  private final RewardPolicyLineRepository rewardPolicyLineRepository;
  private final RewardPolicyApplicableTargetRepository applicableTargetRepository;
  private final RewardRepository rewardRepository;
  private final GroupRewardRepository groupRewardRepository;
  private final RewardMapper rewardMapper;
  private final IExternalService iExternalService;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListRewardPolicyDto>> getAll(Pageable pageable,
      FilterRequest filter) {
    GenericSpecification<RewardPolicyEntity> spec = FilterPolicy.build(filter);
    Page<RewardPolicyEntity> page = rewardPolicyRepository.findAll(spec, pageable);
    return ResponseUtils.toPageResponse(page, page.getContent()
        .stream()
        .map(rewardPolicyMapper::toListDto)
        .toList(), "Get All Reward Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> createRewardPolicy(DetailRewardPolicyDto rewardPolicyDto) {
    RewardPolicyEntity entity = rewardPolicyMapper.toEntity(rewardPolicyDto);
    rewardPolicyRepository.save(entity);
    Long policyId = entity.getId();
    createOrUpdateApplicableTargets(policyId, rewardPolicyDto.getApplicableTargets());
    createOrUpdatePolicyLines(policyId, rewardPolicyDto.getRewardPolicyLines());
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity),
        "Create Reward Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> updateRewardPolicy(DetailRewardPolicyDto rewardPolicyDto) {
    RewardPolicyEntity entity = getRewardPolicyEntity(rewardPolicyDto.getId());
    rewardPolicyMapper.updateRewardPolicy(rewardPolicyDto, entity);
    rewardPolicyRepository.save(entity);
    createOrUpdateApplicableTargets(entity.getId(), rewardPolicyDto.getApplicableTargets());
    createOrUpdatePolicyLines(entity.getId(), rewardPolicyDto.getRewardPolicyLines());
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity), "Update Reward Policy");
  }


  private void createOrUpdateApplicableTargets(Long policyId, List<CommonDto> applicableTargets) {
    List<RewardPolicyApplicableTargetEntity> existingTargets =
        applicableTargetRepository.findByRewardPolicyId(policyId);
    applicableTargetRepository.deleteAll(existingTargets);
    if (applicableTargets == null || applicableTargets.isEmpty()) {
      return;
    }
    List<RewardPolicyApplicableTargetEntity> newTargets = applicableTargets.stream()
        .map(t -> {
          RewardPolicyApplicableTargetEntity entity = new RewardPolicyApplicableTargetEntity();
          entity.setRewardPolicyId(policyId);
          entity.setTargetId(t.getId());
          return entity;
        }).toList();

    applicableTargetRepository.saveAll(newTargets);
  }

  private void createOrUpdatePolicyLines(Long policyId, List<DetailRewardPolicyLineDto> lineDtos) {
    List<RewardPolicyLineEntity> existingLines = rewardPolicyLineRepository.findByRewardPolicyId(
        policyId);
    Map<Long, RewardPolicyLineEntity> existingLineMap = existingLines.stream()
        .filter(line -> line.getId() != null)
        .collect(Collectors.toMap(RewardPolicyLineEntity::getId, Function.identity()));
    List<RewardPolicyLineEntity> linesToSave = new ArrayList<>();
    for (DetailRewardPolicyLineDto lineDto : lineDtos) {
      RewardPolicyLineEntity lineEntity;
      if (lineDto.getId() != null && existingLineMap.containsKey(lineDto.getId())) {
        lineEntity = existingLineMap.get(lineDto.getId());
        rewardPolicyLineMapper.updatePolicyLine(lineDto, lineEntity);
        existingLineMap.remove(lineDto.getId());
      } else {
        lineEntity = rewardPolicyLineMapper.toEntity(lineDto);
        lineEntity.setRewardPolicyId(policyId);
      }
      linesToSave.add(lineEntity);
    }
    if (!existingLineMap.isEmpty()) {
      rewardPolicyLineRepository.deleteAll(existingLineMap.values());
    }
    rewardPolicyLineRepository.saveAll(linesToSave);
  }

  @Override
  public BaseResponse<DetailRewardPolicyDto> getRewardPolicy(Long id) {
    RewardPolicyEntity policyEntity = getRewardPolicyEntity(id);
    DetailRewardPolicyDto rewardPolicyDto = rewardPolicyMapper.toDto(policyEntity);
    List<RewardPolicyLineEntity> lineEntities = rewardPolicyLineRepository.findByRewardPolicyId(id);
    Map<Long, DetailRewardDto> rewardDtoMap = getRewardDtoMap(lineEntities);
    List<DetailRewardPolicyLineDto> lineDtos = buildRewardPolicyLineDtos(lineEntities,
        rewardDtoMap);
    rewardPolicyDto.setRewardPolicyLines(lineDtos);
    ApplicableObject applicableObject = policyEntity.getApplicableType();
    List<CommonDto> applicableTargets = getApplicableTargets(id, applicableObject);
    rewardPolicyDto.setApplicableTargets(applicableTargets);
    return BaseResponse.success(rewardPolicyDto, "Get Reward Policy");
  }

  private Map<Long, DetailRewardDto> getRewardDtoMap(List<RewardPolicyLineEntity> lines) {
    Set<Long> rewardIds = lines.stream().map(RewardPolicyLineEntity::getRewardId)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
    List<RewardEntity> rewardEntities = rewardRepository.findAllById(rewardIds);
    return rewardEntities.stream().map(this::getDataExternal).collect(Collectors.toMap(
        DetailRewardDto::getId,
        Function.identity()
    ));
  }

  private List<DetailRewardPolicyLineDto> buildRewardPolicyLineDtos(
      List<RewardPolicyLineEntity> lines,
      Map<Long, DetailRewardDto> rewardDtoMap) {
    return lines.stream().map(line -> {
      DetailRewardPolicyLineDto lineDto = rewardPolicyLineMapper.toDto(line);
      lineDto.setReward(rewardDtoMap.get(line.getRewardId()));
      return lineDto;
    }).toList();
  }

  private List<CommonDto> getApplicableTargets(Long policyId, ApplicableObject applicableType) {
    if (applicableType == null || applicableType == ApplicableObject.All) {
      return List.of();
    }
    List<RewardPolicyApplicableTargetEntity> targets =
        applicableTargetRepository.findByRewardPolicyId(policyId);
    List<Long> targetIds = targets.stream()
        .map(RewardPolicyApplicableTargetEntity::getTargetId)
        .toList();
    return switch (applicableType) {
      case EMPLOYEE -> iExternalService.getEmployeeInfoByIds(targetIds);
      case DEPARTMENT -> iExternalService.getDepartmentInfoByIds(targetIds);
      case POSITION -> iExternalService.getPositionInfoByIds(targetIds);
      default -> List.of();
    };
  }

  public DetailRewardDto getDataExternal(RewardEntity entity) {
    DetailRewardDto dto = rewardMapper.toDetailDto(entity);
    if (entity.getGroupRewardId() != null) {
      groupRewardRepository.findById(entity.getGroupRewardId())
          .ifPresent(parent -> dto.setGroupReward(rewardMapper.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;
  }

  @Override
  public void deleteRewardPolicy(Long id) {
    RewardPolicyEntity entity = getRewardPolicyEntity(id);
    List<RewardPolicyLineEntity> lineEntities = rewardPolicyLineRepository.findByRewardPolicyId(id);
    rewardPolicyLineRepository.deleteAll(lineEntities);
    List<RewardPolicyApplicableTargetEntity> targetEntities = applicableTargetRepository.findByRewardPolicyId(
        id);
    applicableTargetRepository.deleteAll(targetEntities);
    rewardPolicyRepository.delete(entity);
  }

  private RewardPolicyEntity getRewardPolicyEntity(Long id) {
    return rewardPolicyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward policy id :" + id));
  }


}
