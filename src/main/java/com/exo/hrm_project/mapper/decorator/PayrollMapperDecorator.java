package com.exo.hrm_project.mapper.decorator;

import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.entity.PayrollEntity;
import com.exo.hrm_project.mapper.PayrollMapper;
import com.exo.hrm_project.service.IExternalService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
@RequiredArgsConstructor
public class PayrollMapperDecorator implements PayrollMapper {

  private final PayrollMapper delegate;
  private final IExternalService iExternalService;

  @Override
  public ListPayrollDto toListDto(PayrollEntity entity) {
    ListPayrollDto dto = delegate.toListDto(entity);
    dto.setEmployee(
        iExternalService.getEmployeeInfoByIds(List.of(entity.getEmployeeId())).stream().findFirst()
            .orElse(null));
    dto.setPosition(
        iExternalService.getPositionInfoByIds(List.of(entity.getEmployeeId())).stream().findFirst()
            .orElse(null));
    return dto;
  }

  @Override
  public PayrollDto toDto(PayrollEntity entity) {
    PayrollDto dto = delegate.toDto(entity);
    dto.setEmployee(
        iExternalService.getEmployeeInfoByIds(List.of(entity.getEmployeeId())).stream().findFirst()
            .orElse(null));
    dto.setPosition(
        iExternalService.getPositionInfoByIds(List.of(entity.getPositionId())).stream().findFirst()
            .orElse(null));
    dto.setDepartment(
        iExternalService.getDepartmentInfoByIds(List.of(entity.getDepartmentId())).stream()
            .findFirst()
            .orElse(null));
    return dto;
  }

  @Override
  public PayrollEntity toEntity(FormPayrollDto dto) {
    return delegate.toEntity(dto);
  }

  @Override
  public void updatePayroll(FormPayrollDto dto, PayrollEntity entity) {
    delegate.updatePayroll(dto, entity);
  }
}
