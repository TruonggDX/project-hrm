package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.detail_payroll.AllowanceDtoLine;
import com.exo.hrm_project.dto.detail_payroll.DetailRewardDtoLine;
import com.exo.hrm_project.dto.detail_payroll.GroupAllowancesDto;
import com.exo.hrm_project.dto.detail_payroll.GroupRewardsDto;
import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollLineDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.dto.payroll.PayrolleLineDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.entity.PayrollEntity;
import com.exo.hrm_project.entity.PayrollLineEntity;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.AllowanceMapper;
import com.exo.hrm_project.mapper.PayrollLineMapper;
import com.exo.hrm_project.mapper.PayrollMapper;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.AllowanceRepository;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.repository.PayrollLineRepository;
import com.exo.hrm_project.repository.PayrollRepository;
import com.exo.hrm_project.repository.RewardRepository;
import com.exo.hrm_project.service.IPayrolleService;
import com.exo.hrm_project.utils.enums.PayrollLineType;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements IPayrolleService {

  private final PayrollRepository payrollRepository;
  private final PayrollMapper payrollMapper;
  private final PayrollLineMapper payrollLineMapper;
  private final PayrollLineRepository payrollLineRepository;
  private final GroupAllowanceRepository groupAllowanceRepository;
  private final GroupRewardRepository groupRewardRepository;
  private final RewardRepository rewardRepository;
  private final AllowanceRepository allowanceRepository;
  private final RewardMapper rewardMapper;
  private final AllowanceMapper allowanceMapper;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListPayrollDto>> getAll(Pageable pageable) {
    Page<PayrollEntity> page = payrollRepository.findAll(pageable);
    List<ListPayrollDto> dtos = page.getContent()
        .stream().map(entity -> {
          ListPayrollDto listPayrollDto = payrollMapper.toListDto(entity);
          List<PayrollLineEntity> list = payrollLineRepository.findPayrollById(entity.getId());
          List<PayrolleLineDto> lineDtos = list.stream().map(payrollLineMapper::toDto).toList();
          listPayrollDto.setPayrollLine(lineDtos);
          return listPayrollDto;
        }).toList();
    return ResponseUtils.toPageResponse(page, dtos, "Get All Payroll");
  }

  @Transactional
  @Override
  public BaseResponse<ResponseCommon> createPayroll(FormPayrollDto formPayrollDto) {
    PayrollEntity payrollEntity = payrollMapper.toEntity(formPayrollDto);
    payrollEntity.setEmployeeId(formPayrollDto.getEmployee().getId());
    payrollEntity.setDepartmentId(formPayrollDto.getDepartment().getId());
    payrollEntity.setPositionId(formPayrollDto.getPosition().getId());
    payrollEntity = payrollRepository.save(payrollEntity);
    BigDecimal totalAmount = calculateTotalAmount(formPayrollDto.getPayrollLine());
    payrollEntity.setTotalAllowanceAmount(totalAmount);
    Long payrollId = payrollEntity.getId();
    saved(formPayrollDto, payrollId);
    return BaseResponse.success(genericIdMapper.toResponseCommon(payrollEntity),
        "Create Payroll Successfully");
  }

  private BigDecimal calculateTotalAmount(List<FormPayrollLineDto> lines) {
    BigDecimal total = BigDecimal.ZERO;
    for (FormPayrollLineDto dto : lines) {
      total = total.add(dto.getAmount());
      total = total.add(dto.getTaxableAmount());
      total = total.add(dto.getInsuranceAmount());
    }
    return total;
  }

  private void saved(FormPayrollDto formPayrollDto, Long payrollId) {
    if (formPayrollDto.getPayrollLine() != null) {
      List<PayrollLineEntity> payrollLineEntities = formPayrollDto.getPayrollLine().stream()
          .map(dto -> {
            PayrollLineEntity lineEntity = payrollLineMapper.toEntity(dto);
            lineEntity.setPayrollId(payrollId);
            if (dto.getTarget() != null && dto.getTarget().getId() != null) {
              lineEntity.setGroupId(dto.getTarget().getId());
            }
            if (dto.getGroupTarget() != null && dto.getGroupTarget().getId() != null) {
              lineEntity.setGroupTargetId(dto.getGroupTarget().getId());
            }
            return lineEntity;
          })
          .toList();
      payrollLineRepository.saveAll(payrollLineEntities);
    }
  }

  @Transactional
  @Override
  public BaseResponse<ResponseCommon> updatePayroll(FormPayrollDto formPayrollDto) {
    PayrollEntity payrollEntity = payrollRepository.findById(formPayrollDto.getId())
        .orElseThrow(() -> new NotFoundException("Not found payroll: " + formPayrollDto.getId()));
    payrollMapper.updatePayroll(formPayrollDto, payrollEntity);
    payrollEntity.setEmployeeId(formPayrollDto.getEmployee().getId());
    payrollEntity.setDepartmentId(formPayrollDto.getDepartment().getId());
    payrollEntity.setPositionId(formPayrollDto.getPosition().getId());
    BigDecimal totalAmount = calculateTotalAmount(formPayrollDto.getPayrollLine());
    payrollEntity.setTotalAllowanceAmount(totalAmount);
    payrollRepository.save(payrollEntity);
    updatePayrollLines(formPayrollDto.getPayrollLine(), payrollEntity.getId());
    return BaseResponse.success(
        genericIdMapper.toResponseCommon(payrollEntity),
        "Update Payroll Successfully"
    );
  }

  private void updatePayrollLines(List<FormPayrollLineDto> lineDtos, Long payrollId) {

    List<PayrollLineEntity> result = new ArrayList<>();
    for (FormPayrollLineDto dto : lineDtos) {
      PayrollLineEntity entity;
      if (dto.getId() != null) {
        entity = payrollLineRepository.findById(dto.getId())
            .orElseThrow(() -> new NotFoundException("Not found payroll line: " + dto.getId()));
        payrollLineMapper.updatePayrollLine(dto, entity);
      } else {
        entity = payrollLineMapper.toEntity(dto);
      }
      entity.setPayrollId(payrollId);
      if (dto.getGroupTarget() != null) {
        entity.setGroupTargetId(dto.getGroupTarget().getId());
      }
      if (dto.getTarget() != null) {
        entity.setGroupId(dto.getTarget().getId());
      }
      result.add(entity);
    }
    payrollLineRepository.saveAll(result);
  }


  @Override
  public BaseResponse<PayrollDto> getPayrollById(Long id) {
    PayrollEntity payrollEntity = payrollRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found id payroll :" + id));
    PayrollDto payrollDto = payrollMapper.toDto(payrollEntity);
    List<PayrollLineEntity> payrollLines = payrollLineRepository.findPayrollById(
        payrollEntity.getId());
    processAllowance(payrollDto, payrollLines);
    processReward(payrollDto, payrollLines);
    return BaseResponse.success(payrollDto, "Get Payroll successfully");
  }


  private void processAllowance(PayrollDto payrollDto, List<PayrollLineEntity> payrollLines) {
    List<PayrollLineEntity> allowance = payrollLines.stream()
        .filter(line -> line.getType() == PayrollLineType.ALLOWANCE).toList();
    List<Long> groupIds = allowance.stream()
        .map(PayrollLineEntity::getGroupId).distinct().toList();
    List<GroupAllowanceEntity> groups = groupAllowanceRepository.findAllByIdIn(groupIds);
    List<String> groupNames = groups.stream()
        .map(GroupAllowanceEntity::getName).distinct().toList();
    if (payrollDto.getGroupAllowances() == null) {
      payrollDto.setGroupAllowances(new GroupAllowancesDto());
    }
    payrollDto.getGroupAllowances().setAllGroupName(groupNames);
    List<AllowanceDtoLine> lines = allowance.stream()
        .map(entity -> {
          AllowanceDtoLine dto = payrollLineMapper.toAllowanceDtoLine(entity);
          AllowanceEntity allowanceEntity = allowanceRepository.findById(entity.getGroupId())
              .orElseThrow(() -> new NotFoundException("Allowance not found"));
          dto.setAllowance(allowanceMapper.toDetailsDto(allowanceEntity));
          return dto;
        })
        .toList();
    payrollDto.getGroupAllowances().setAllowanceLine(lines);
  }

  private void processReward(PayrollDto payrollDto, List<PayrollLineEntity> payrollLines) {
    List<PayrollLineEntity> reward = payrollLines.stream()
        .filter(line -> line.getType() == PayrollLineType.REWARD).toList();
    List<Long> groupIds = reward.stream()
        .map(PayrollLineEntity::getGroupId).distinct().toList();
    List<GroupRewardEntity> groups = groupRewardRepository.findAllByIdIn(groupIds);
    List<String> groupNames = groups.stream()
        .map(GroupRewardEntity::getName).distinct().toList();
    if (payrollDto.getGroupRewards() == null) {
      payrollDto.setGroupRewards(new GroupRewardsDto());
    }
    payrollDto.getGroupRewards().setAllGroupName(groupNames);
    List<DetailRewardDtoLine> lines = reward.stream()
        .map(entity -> {
          DetailRewardDtoLine dto = payrollLineMapper.toRewardDtoLine(entity);
          RewardEntity rewardEntity = rewardRepository.findById(entity.getGroupId())
              .orElseThrow(() -> new NotFoundException("Reward not found"));
          dto.setReward(rewardMapper.toDetailsDto(rewardEntity));
          return dto;
        })
        .toList();
    payrollDto.getGroupRewards().setReawardLine(lines);
  }

  @Override
  public void deletePayrollById(Long id) {
    PayrollEntity payrollEntity = payrollRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found id payroll :" + id));
    List<PayrollLineEntity> payrollLineEntity = payrollLineRepository.findPayrollById(id);
    payrollLineRepository.deleteAll(payrollLineEntity);
    payrollRepository.delete(payrollEntity);
  }

}
