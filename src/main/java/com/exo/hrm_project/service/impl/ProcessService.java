package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.detail_payroll.AllowanceDtoLine;
import com.exo.hrm_project.dto.detail_payroll.DetailRewardDtoLine;
import com.exo.hrm_project.dto.detail_payroll.DetailsAllowanceDto;
import com.exo.hrm_project.dto.detail_payroll.DetailsRewardDto;
import com.exo.hrm_project.dto.detail_payroll.GroupAllowancesDto;
import com.exo.hrm_project.dto.detail_payroll.GroupRewardsDto;
import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.dto.payroll.PayrolleLineDto;
import com.exo.hrm_project.dto.payroll.TargetPolicyLine;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.entity.PayrollEntity;
import com.exo.hrm_project.entity.PayrollLineEntity;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.entity.RewardPolicyLineEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.AllowanceMapper;
import com.exo.hrm_project.mapper.GroupAllowanceMapper;
import com.exo.hrm_project.mapper.GroupRewardMapper;
import com.exo.hrm_project.mapper.PayrollLineMapper;
import com.exo.hrm_project.mapper.PayrollMapper;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.repository.AllowancePolicyLineRepository;
import com.exo.hrm_project.repository.AllowanceRepository;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.repository.RewardPolicyLineRepository;
import com.exo.hrm_project.repository.RewardRepository;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.utils.enums.PayrollLineType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProcessService {

  private final GroupAllowanceRepository groupAllowanceRepository;
  private final GroupRewardRepository groupRewardRepository;
  private final RewardRepository rewardRepository;
  private final AllowanceRepository allowanceRepository;
  private final GroupAllowanceMapper groupAllowanceMapper;
  private final RewardMapper rewardMapper;
  private final GroupRewardMapper groupRewardMapper;
  private final AllowanceMapper allowanceMapper;
  private final PayrollLineMapper payrollLineMapper;
  private final RewardPolicyLineRepository rewardPolicyLineRepository;
  private final PayrollMapper payrollMapper;
  private final IExternalService iExternalService;
  private final AllowancePolicyLineRepository policyLineRepository;

  private <T, ID> Map<ID, T> mapEntitiesById(
      List<ID> ids,
      Function<List<ID>, List<T>> finder,
      Function<T, ID> idGetter
  ) {
    if (ids == null || ids.isEmpty()) {
      return Map.of();
    }
    return finder.apply(ids).stream()
        .collect(Collectors.toMap(idGetter, Function.identity()));
  }

  public void processAllowance(PayrollDto payrollDto, List<PayrollLineEntity> payrollLines) {
    List<PayrollLineEntity> allowanceLines = payrollLines.stream()
        .filter(line -> line.getType() == PayrollLineType.ALLOWANCE).toList();
    List<Long> groupAllowanceIds = allowanceLines.stream()
        .map(PayrollLineEntity::getGroupId).distinct().toList();
    List<Long> allowanceIds = allowanceLines.stream()
        .map(PayrollLineEntity::getGroupId).distinct().toList();
    Map<Long, GroupAllowanceEntity> groupAllowanceMap = mapEntitiesById(
        groupAllowanceIds, groupAllowanceRepository::findAllByIdIn, GroupAllowanceEntity::getId);
    Map<Long, AllowanceEntity> allowanceMap = mapEntitiesById(
        allowanceIds, allowanceRepository::findAllByIdIn, AllowanceEntity::getId);
    List<CommonDto> groupAllowanceDtos = groupAllowanceMap.values().stream()
        .map(groupAllowanceMapper::toCommonDto)
        .toList();
    if (payrollDto.getGroupAllowances() == null) {
      payrollDto.setGroupAllowances(new GroupAllowancesDto());
    }
    payrollDto.getGroupAllowances().setGroupAllowances(groupAllowanceDtos);
    List<AllowanceDtoLine> allowanceDtoLines = allowanceLines.stream()
        .map(entity -> {
          AllowanceDtoLine dto = payrollLineMapper.toAllowanceDtoLine(entity);
          AllowanceEntity allowanceEntity = allowanceMap.get(entity.getGroupId());
          if (allowanceEntity == null) {
            throw new NotFoundException("Allowance id not found: " + entity.getGroupId());
          }
          dto.setAllowance(buildAllowance(allowanceEntity, groupAllowanceMap));
          return dto;
        })
        .toList();

    payrollDto.getGroupAllowances().setAllowanceLines(allowanceDtoLines);
  }

  public void processReward(PayrollDto payrollDto, List<PayrollLineEntity> payrollLines) {
    List<PayrollLineEntity> rewardLines = payrollLines.stream()
        .filter(line -> line.getType() == PayrollLineType.REWARD).toList();
    List<Long> groupRewardIds = rewardLines.stream()
        .map(PayrollLineEntity::getGroupId).distinct().toList();
    List<Long> rewardIds = rewardLines.stream()
        .map(PayrollLineEntity::getGroupId).distinct().toList();
    Map<Long, GroupRewardEntity> groupRewardMap = mapEntitiesById(
        groupRewardIds, groupRewardRepository::findAllByIdIn, GroupRewardEntity::getId);
    Map<Long, RewardEntity> rewardMap = mapEntitiesById(
        rewardIds, rewardRepository::findAllByIdIn, RewardEntity::getId);
    List<CommonDto> groupRewardDtos = groupRewardMap.values().stream()
        .map(groupRewardMapper::toCommonDto).toList();
    if (payrollDto.getGroupRewards() == null) {
      payrollDto.setGroupRewards(new GroupRewardsDto());
    }
    payrollDto.getGroupRewards().setGroupRewards(groupRewardDtos);
    List<DetailRewardDtoLine> rewardDtoLines = rewardLines.stream()
        .map(entity -> {
          DetailRewardDtoLine dto = payrollLineMapper.toRewardDtoLine(entity);
          RewardEntity rewardEntity = rewardMap.get(entity.getGroupId());
          if (rewardEntity == null) {
            throw new NotFoundException("Reward id not found: " + entity.getGroupId());
          }
          dto.setReward(buildReward(rewardEntity, groupRewardMap));
          return dto;
        })
        .toList();
    payrollDto.getGroupRewards().setRewardLines(rewardDtoLines);
  }

  private DetailsAllowanceDto buildAllowance(AllowanceEntity entity,
      Map<Long, GroupAllowanceEntity> groupAllowanceMap) {
    DetailsAllowanceDto dto = allowanceMapper.toDetailsDto(entity);
    if (entity.getGroupAllowanceId() != null) {
      GroupAllowanceEntity parent = groupAllowanceMap.get(entity.getGroupAllowanceId());
      if (parent != null) {
        dto.setGroupAllowance(allowanceMapper.toCommonDto(parent));
      }
    }
    return dto;
  }

  private DetailsRewardDto buildReward(RewardEntity entity,
      Map<Long, GroupRewardEntity> groupRewardMap) {
    DetailsRewardDto dto = rewardMapper.toDetailsDto(entity);
    if (entity.getGroupRewardId() != null) {
      GroupRewardEntity parent = groupRewardMap.get(entity.getGroupRewardId());
      if (parent != null) {
        dto.setGroupReward(rewardMapper.toCommonDto(parent));
      }
    }
    return dto;
  }

  public PayrollDto buildPayrollDto(PayrollEntity entity) {
    PayrollDto dto = payrollMapper.toDto(entity);
    CompletableFuture<CommonDto> empFuture = CompletableFuture.supplyAsync(() ->
        iExternalService.getEmployeeInfoByIds(List.of(entity.getEmployeeId()))
            .stream().findFirst().orElse(null));
    CompletableFuture<CommonDto> posFuture = CompletableFuture.supplyAsync(() ->
        iExternalService.getPositionInfoByIds(List.of(entity.getPositionId()))
            .stream().findFirst().orElse(null));
    CompletableFuture<CommonDto> depFuture = CompletableFuture.supplyAsync(() ->
        iExternalService.getDepartmentInfoByIds(List.of(entity.getDepartmentId()))
            .stream().findFirst().orElse(null));
    CompletableFuture.allOf(empFuture, posFuture, depFuture).join();
    dto.setEmployee(empFuture.join());
    dto.setPosition(posFuture.join());
    dto.setDepartment(depFuture.join());

    return dto;
  }

  public ListPayrollDto buildListPayrollWithLines(PayrollEntity entity,
      List<PayrollLineEntity> lines) {
    ListPayrollDto dto = buildListPayrollBase(entity);
    List<PayrolleLineDto> lineDtos = lines != null
        ? lines.stream().map(this::buildPayrollLine).toList()
        : Collections.emptyList();
    dto.setPayrollLines(lineDtos);
    return dto;
  }

  public ListPayrollDto buildListPayrollBase(PayrollEntity entity) {
    ListPayrollDto dto = payrollMapper.toListDto(entity);

    CompletableFuture<CommonDto> empFuture = CompletableFuture.supplyAsync(() ->
        iExternalService.getEmployeeInfoByIds(List.of(entity.getEmployeeId()))
            .stream().findFirst().orElse(null));

    CompletableFuture<CommonDto> posFuture = CompletableFuture.supplyAsync(() ->
        iExternalService.getPositionInfoByIds(List.of(entity.getPositionId()))
            .stream().findFirst().orElse(null));

    CompletableFuture.allOf(empFuture, posFuture).join();
    dto.setEmployee(empFuture.join());
    dto.setPosition(posFuture.join());

    return dto;
  }

  public PayrolleLineDto buildPayrollLine(PayrollLineEntity entity) {
    PayrolleLineDto dto = payrollLineMapper.toDto(entity);

    switch (entity.getType()) {
      case ALLOWANCE -> {
        AllowanceEntity allowance = allowanceRepository.findById(entity.getGroupId()).orElse(null);
        if (allowance != null) {
          dto.setTargetName(allowance.getName());
          AllowancePolicyLineEntity policy = policyLineRepository.findByAllowanceId(
              allowance.getId());
          if (policy != null) {
            TargetPolicyLine targetPolicyLine = new TargetPolicyLine();
            targetPolicyLine.setCycle(policy.getCycle().name());
            dto.setTargetPolicyLine(targetPolicyLine);
          }
        }
      }
      case REWARD -> {
        RewardEntity reward = rewardRepository.findById(entity.getGroupId()).orElse(null);
        if (reward != null) {
          dto.setTargetName(reward.getName());
          RewardPolicyLineEntity policy = rewardPolicyLineRepository.findByRewardId(reward.getId());
          if (policy != null) {
            TargetPolicyLine targetPolicyLine = new TargetPolicyLine();
            targetPolicyLine.setCycle(policy.getCycle().name());
            dto.setTargetPolicyLine(targetPolicyLine);
          }
        }
      }
    }
    return dto;
  }

}
