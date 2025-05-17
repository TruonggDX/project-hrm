package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.common.CommonDto;
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
}
