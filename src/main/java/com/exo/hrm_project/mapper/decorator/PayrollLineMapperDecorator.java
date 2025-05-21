package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.detail_payroll.AllowanceDtoLine;
import com.exo.hrm_project.dto.detail_payroll.DetailRewardDtoLine;
import com.exo.hrm_project.dto.payroll.FormPayrollLineDto;
import com.exo.hrm_project.dto.payroll.PayrolleLineDto;
import com.exo.hrm_project.dto.payroll.TargetPolicyLine;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import com.exo.hrm_project.entity.PayrollLineEntity;
import com.exo.hrm_project.mapper.PayrollLineMapper;
import com.exo.hrm_project.repository.AllowancePolicyLineRepository;
import com.exo.hrm_project.repository.AllowanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class PayrollLineMapperDecorator implements PayrollLineMapper {

  private final PayrollLineMapper delegate;
  private final AllowanceRepository allowanceRepository;
  private final AllowancePolicyLineRepository lineRepository;

  @Override
  public PayrolleLineDto toDto(PayrollLineEntity entity) {
    PayrolleLineDto dto = delegate.toDto(entity);
    AllowanceEntity allowanceEntity = allowanceRepository.findById(entity.getGroupId())
        .orElse(null);
    if (allowanceEntity != null) {
      dto.setTargetName(allowanceEntity.getName());
      AllowancePolicyLineEntity policyLineEntity = lineRepository.findByAllowanceId(
          allowanceEntity.getId());
      if (policyLineEntity != null) {
        TargetPolicyLine targetPolicyLine = new TargetPolicyLine();
        targetPolicyLine.setCycle(policyLineEntity.getCycle().name());
        dto.setTargetPolicyLine(targetPolicyLine);
      }
    }
    return dto;
  }

  @Override
  public DetailRewardDtoLine toRewardDtoLine(PayrollLineEntity entity) {
    return delegate.toRewardDtoLine(entity);
  }

  @Override
  public AllowanceDtoLine toAllowanceDtoLine(PayrollLineEntity entity) {
    return delegate.toAllowanceDtoLine(entity);
  }

  @Override
  public PayrollLineEntity toEntity(FormPayrollLineDto formPayrollLineDto) {
    return delegate.toEntity(formPayrollLineDto);
  }

  @Override
  public void updatePayrollLine(FormPayrollLineDto formPayrollLineDto, PayrollLineEntity entity) {
    delegate.updatePayrollLine(formPayrollLineDto, entity);
  }
}
