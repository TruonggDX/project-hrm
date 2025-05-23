package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.PayrollEntity;
import com.exo.hrm_project.entity.PayrollLineEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.PayrollLineMapper;
import com.exo.hrm_project.mapper.PayrollMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.PayrollLineRepository;
import com.exo.hrm_project.repository.PayrollRepository;
import com.exo.hrm_project.service.IPayrolleService;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
  private final GenericIdMapper genericIdMapper;
  private final ProcessService processService;


  @Override
  public BaseResponse<ResponsePage<ListPayrollDto>> getAll(Pageable pageable) {
    Page<PayrollEntity> page = payrollRepository.findAll(pageable);
    List<Long> payrollIds = page.getContent().stream()
        .map(PayrollEntity::getId).toList();
    List<PayrollLineEntity> allLines = payrollLineRepository.findByPayrollIds(payrollIds);
    Map<Long, List<PayrollLineEntity>> linesMap = allLines.stream()
        .collect(Collectors.groupingBy(PayrollLineEntity::getPayrollId));
    List<ListPayrollDto> dtos = page.getContent().stream()
        .map(entity -> processService.buildListPayrollWithLines(entity,
            linesMap.get(entity.getId())))
        .toList();
    return ResponseUtils.toPageResponse(page, dtos, "Get All Payroll");
  }


  @Transactional
  @Override
  public BaseResponse<ResponseCommon> createPayroll(PayrollDto formPayrollDto) {
    PayrollEntity payrollEntity = payrollMapper.toEntity(formPayrollDto);
    payrollEntity = payrollRepository.save(payrollEntity);
    Long payrollId = payrollEntity.getId();
    saveOrUpdateLines(formPayrollDto, payrollId);
    return BaseResponse.success(
        genericIdMapper.toResponseCommon(payrollEntity),
        "Create Payroll Successfully"
    );
  }

  @Transactional
  @Override
  public BaseResponse<ResponseCommon> updatePayroll(PayrollDto formPayrollDto) {
    PayrollEntity payrollEntity = getPayrollEntityById(formPayrollDto.getId());
    payrollMapper.updatePayroll(formPayrollDto, payrollEntity);
    payrollRepository.save(payrollEntity);
    Long payrollId = payrollEntity.getId();
    saveOrUpdateLines(formPayrollDto, payrollId);
    return BaseResponse.success(
        genericIdMapper.toResponseCommon(payrollEntity),
        "Update Payroll Successfully"
    );
  }

  private void saveOrUpdateLines(PayrollDto dto, Long payrollId) {
    if (dto.getGroupAllowances() != null && dto.getGroupAllowances().getAllowanceLines() != null) {
      List<PayrollLineEntity> allowanceLines = dto.getGroupAllowances().getAllowanceLines().stream()
          .map(line -> {
            PayrollLineEntity entity = payrollLineMapper.toAllowanceEntity(line);
            entity.setPayrollId(payrollId);
            return entity;
          }).toList();
      payrollLineRepository.saveAll(allowanceLines);
    }
    if (dto.getGroupRewards() != null && dto.getGroupRewards().getRewardLines() != null) {
      List<PayrollLineEntity> rewardLines = dto.getGroupRewards().getRewardLines().stream()
          .map(line -> {
            PayrollLineEntity entity = payrollLineMapper.toRewardEntity(line);
            entity.setPayrollId(payrollId);
            return entity;
          }).toList();
      payrollLineRepository.saveAll(rewardLines);
    }
  }

  @Override
  public BaseResponse<PayrollDto> getPayrollById(Long id) {
    PayrollEntity payrollEntity = getPayrollEntityById(id);
    PayrollDto payrollDto = processService.buildPayrollDto(payrollEntity);
    List<PayrollLineEntity> payrollLines = payrollLineRepository.findPayrollById(
        payrollEntity.getId());
    processService.processAllowance(payrollDto, payrollLines);
    processService.processReward(payrollDto, payrollLines);
    return BaseResponse.success(payrollDto, "Get payroll Successfully");
  }


  @Override
  public void deletePayrollById(Long id) {
    PayrollEntity payrollEntity = getPayrollEntityById(id);
    List<PayrollLineEntity> payrollLineEntity = payrollLineRepository.findPayrollById(id);
    payrollLineRepository.deleteAll(payrollLineEntity);
    payrollRepository.delete(payrollEntity);
  }

  private PayrollEntity getPayrollEntityById(Long id) {
    return payrollRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found id payroll :" + id));
  }

}
