package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward_policy.DetailRewardDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyLineDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.RewardPolicyDto;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.entity.RewardPolicyApplicableTargetEntity;
import com.exo.hrm_project.entity.RewardPolicyEntity;
import com.exo.hrm_project.entity.RewardPolicyLineEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.mapper.RewardPolicyLineMapper;
import com.exo.hrm_project.mapper.RewardPolicyMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.RewardPolicyApplicableTargetRepository;
import com.exo.hrm_project.repository.RewardPolicyLineRepository;
import com.exo.hrm_project.repository.RewardPolicyRepository;
import com.exo.hrm_project.repository.RewardRepository;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.service.IRewardPolicyService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;
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
public class RewardPolicyServiceImpl implements IRewardPolicyService {

  private final RewardPolicyRepository rewardPolicyRepository;
  private final RewardPolicyMapper rewardPolicyMapper;
  private final RewardPolicyLineMapper rewardPolicyLineMapper;
  private final RewardPolicyLineRepository rewardPolicyLineRepository;
  private final RewardPolicyApplicableTargetRepository applicableTargetRepository;
  private final RewardRepository rewardRepository;
  private final RewardMapper rewardMapper;
  private final IExternalService iExternalService;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListRewardPolicyDto>> getAll(Pageable pageable,
      ListRewardPolicyDto filter) {
    GenericSpecification<RewardPolicyEntity> spec = new GenericSpecification<>();
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
    Page<RewardPolicyEntity> page = rewardPolicyRepository.findAll(spec, pageable);
    List<ListRewardPolicyDto> dtos = page.getContent()
        .stream()
        .map(rewardPolicyMapper::toListDto)
        .toList();
    return ResponseUtils.toPageResponse(page, dtos, "Get All Reward Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> createRewardPolicy(RewardPolicyDto rewardPolicyDto) {
    RewardPolicyEntity entity = savePolicy(rewardPolicyDto);
    Long policyId = entity.getId();
    saveTargets(rewardPolicyDto, policyId);
    savePolicyLines(rewardPolicyDto, policyId);
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity),
        "Create Reward Policy");
  }

  private RewardPolicyEntity savePolicy(RewardPolicyDto rewardPolicyDto) {
    RewardPolicyEntity entity = rewardPolicyMapper.toEntity(rewardPolicyDto);
    return rewardPolicyRepository.save(entity);
  }

  private void saveTargets(RewardPolicyDto dto, Long policyId) {
    if (dto.getTarget() == null || dto.getTarget().isEmpty()) {
      return;
    }
    List<RewardPolicyApplicableTargetEntity> targets = dto.getTarget().stream()
        .map(t -> {
          RewardPolicyApplicableTargetEntity entity = new RewardPolicyApplicableTargetEntity();
          entity.setRewardPolicyId(policyId);
          entity.setTargetId(t.getId());
          return entity;
        }).toList();
    applicableTargetRepository.saveAll(targets);
  }

  private void savePolicyLines(RewardPolicyDto dto, Long policyId) {
    if (dto.getRewardPolicyLine() == null || dto.getRewardPolicyLine().isEmpty()) {
      return;
    }
    List<RewardPolicyLineEntity> lines = dto.getRewardPolicyLine().stream()
        .map(lineDto -> {
          RewardPolicyLineEntity entity = rewardPolicyLineMapper.toEntity(lineDto);
          entity.setRewardPolicyId(policyId);
          return entity;
        }).toList();
    rewardPolicyLineRepository.saveAll(lines);
  }

  @Override
  public BaseResponse<ResponseCommon> updateRewardPolicy(RewardPolicyDto rewardPolicyDto) {
    RewardPolicyEntity entity = rewardPolicyRepository.findById(rewardPolicyDto.getId())
        .orElseThrow(() -> new NotFoundException("Policy not found id " + rewardPolicyDto.getId()));
    rewardPolicyMapper.updateRewardPolicy(rewardPolicyDto, entity);
    rewardPolicyRepository.save(entity);
    updateApplicableTargets(rewardPolicyDto);
    updateRewardPolicyLines(rewardPolicyDto);
    return BaseResponse.success(genericIdMapper.toResponseCommon(entity), "Update Reward Policy");
  }


  private void updateApplicableTargets(RewardPolicyDto rewardPolicyDto) {
    List<RewardPolicyApplicableTargetEntity> oldTargets =
        applicableTargetRepository.findByRewardPolicyId(rewardPolicyDto.getId());
    applicableTargetRepository.deleteAll(oldTargets);
    saveTargets(rewardPolicyDto, rewardPolicyDto.getId());
  }

  private void updateRewardPolicyLines(RewardPolicyDto rewardPolicyDto) {
    List<RewardPolicyLineEntity> lineEntities = rewardPolicyDto.getRewardPolicyLine().stream()
        .map(lineDto -> {
          RewardPolicyLineEntity lineEntity;
          if (lineDto.getId() != null) {
            lineEntity = rewardPolicyLineRepository.findById(lineDto.getId())
                .orElseThrow(() -> new NotFoundException("Line not found id " + lineDto.getId()));
            rewardPolicyLineMapper.updatePolicyLine(lineDto, lineEntity);
          } else {
            lineEntity = rewardPolicyLineMapper.toEntity(lineDto);
          }
          lineEntity.setRewardPolicyId(rewardPolicyDto.getId());
          return lineEntity;
        }).toList();
    rewardPolicyLineRepository.saveAll(lineEntities);
  }


  @Override
  public BaseResponse<DetailRewardPolicyDto> getRewardPolicy(Long id) {
    RewardPolicyEntity policyEntity = rewardPolicyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward policy id :" + id));
    DetailRewardPolicyDto rewardPolicyDto = rewardPolicyMapper.toDto(policyEntity);
    List<RewardPolicyLineEntity> lineEntities = rewardPolicyLineRepository.findByRewardPolicyId(id);
    List<DetailRewardPolicyLineDto> lineDtos = lineEntities.stream().map(line -> {
      DetailRewardPolicyLineDto lineDto = rewardPolicyLineMapper.toDto(line);
      RewardEntity rewardEntity = rewardRepository.findById(line.getRewardId()).orElseThrow(
          () -> new NotFoundException("Not found reward id :" + line.getRewardId()));
      if (rewardEntity != null) {
        DetailRewardDto detailRewardDto = rewardMapper.toDetailDto(rewardEntity);
        lineDto.setReward(detailRewardDto);
      }
      return lineDto;
    }).toList();
    rewardPolicyDto.setRewardPolicyLine(lineDtos);
    String type = rewardPolicyDto.getApplicableType();
    if (type != null && !type.isBlank() && !type.equalsIgnoreCase("ALL")) {
      List<CommonDto> applicableTargets = fetchApplicableTargets(id, type);
      rewardPolicyDto.setApplicableTarget(applicableTargets);
    }
    return BaseResponse.success(rewardPolicyDto, "Get Reward Policy");
  }


  private List<CommonDto> fetchApplicableTargets(Long policyId, String applicableType) {
    List<RewardPolicyApplicableTargetEntity> targets =
        applicableTargetRepository.findByRewardPolicyId(policyId);
    List<Long> ids = targets.stream()
        .map(RewardPolicyApplicableTargetEntity::getTargetId)
        .toList();
    return switch (applicableType.toUpperCase()) {
      case "EMPLOYEE" -> iExternalService.getEmployeeInfoByIds(ids);
      case "DEPARTMENT" -> iExternalService.getDepartmentInfoByIds(ids);
      case "POSITION" -> iExternalService.getPositionInfoByIds(ids);
      default -> List.of();
    };
  }

  @Override
  public void deleteRewardPolicy(Long id) {
    RewardPolicyEntity entity = rewardPolicyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward policy id :" + id));
    List<RewardPolicyLineEntity> lineEntities = rewardPolicyLineRepository.findByRewardPolicyId(
        id);
    rewardPolicyLineRepository.deleteAll(lineEntities);
    List<RewardPolicyApplicableTargetEntity> targetEntities = applicableTargetRepository.findByRewardPolicyId(
        id);
    applicableTargetRepository.deleteAll(targetEntities);
    rewardPolicyRepository.delete(entity);
  }

}
