package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.entity.PayrollEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

  ListPayrollDto toListDto(PayrollEntity entity);

  PayrollDto toDto(PayrollEntity entity);

  PayrollEntity toEntity(FormPayrollDto dto);

  void updatePayroll(FormPayrollDto dto, @MappingTarget PayrollEntity entity);
}
