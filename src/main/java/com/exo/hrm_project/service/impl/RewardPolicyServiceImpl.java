package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward_policy.DetailRewardDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyLineDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.RewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.RewardPolicyLineDto;
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
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    RewardPolicyEntity entity = savePolicy(rewardPolicyDto);
    Long policyId = entity.getId();
    saveTargets(rewardPolicyDto, policyId);
    savePolicyLines(rewardPolicyDto, policyId);
    response.setCode(HttpStatus.CREATED.value());
    response.setMessage("Create Allowance Policy");
    response.setData(genericIdMapper.toResponseCommon(entity));
    return response;
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
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    Optional<RewardPolicyEntity> check = rewardPolicyRepository.findById(rewardPolicyDto.getId());
    if (check.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage("Policy not found id " + rewardPolicyDto.getId());
      return response;
    }
    RewardPolicyEntity entity = check.get();
    rewardPolicyMapper.updateRewardPolicy(rewardPolicyDto, entity);
    rewardPolicyRepository.save(entity);
    List<RewardPolicyApplicableTargetEntity> applicableTargetEntities = applicableTargetRepository.findByRewardPolicyId(
        rewardPolicyDto.getId());
    applicableTargetRepository.deleteAll(applicableTargetEntities);
    saveTargets(rewardPolicyDto, rewardPolicyDto.getId());
    List<RewardPolicyLineEntity> lineEntities = new ArrayList<>();
    for (RewardPolicyLineDto lineDto : rewardPolicyDto.getRewardPolicyLine()) {
      Optional<RewardPolicyLineEntity> checkPolicyLine = rewardPolicyLineRepository.findById(
          lineDto.getId());
      if (checkPolicyLine.isEmpty()) {
        response.setCode(HttpStatus.NOT_FOUND.value());
        response.setMessage("Line not found id " + lineDto.getId());
        return response;
      }
      RewardPolicyLineEntity lineEntity = checkPolicyLine.get();
      rewardPolicyLineMapper.updatePolicyLine(lineDto, lineEntity);
      lineEntity.setRewardPolicyId(rewardPolicyDto.getId());
      lineEntities.add(lineEntity);
    }
    rewardPolicyLineRepository.saveAll(lineEntities);
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Update Reward Policy");
    response.setData(genericIdMapper.toResponseCommon(entity));
    return response;
  }

  @Override
  public BaseResponse<DetailRewardPolicyDto> getRewardPolicy(Long id) {
    BaseResponse<DetailRewardPolicyDto> response = new BaseResponse<>();
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
    if (type != null && !type.isBlank()) {
      List<CommonDto> applicableTargets = fetchApplicableTargets(id, type);
      rewardPolicyDto.setApplicableTarget(applicableTargets);
    }
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Get Reward Policy");
    response.setData(rewardPolicyDto);
    return response;
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
      case "ALL" -> List.of();
      default -> List.of();
    };
  }

  @Override
  public void deleteRewardPolicy(Long id) {
    RewardPolicyEntity entity = rewardPolicyRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward policy id :" + id));
    Long policyId = entity.getId();
    List<RewardPolicyLineEntity> lineEntities = rewardPolicyLineRepository.findByRewardPolicyId(
        policyId);
    rewardPolicyLineRepository.deleteAll(lineEntities);
    List<RewardPolicyApplicableTargetEntity> targetEntities = applicableTargetRepository.findByRewardPolicyId(
        policyId);
    applicableTargetRepository.deleteAll(targetEntities);
    rewardPolicyRepository.delete(entity);
  }

}
