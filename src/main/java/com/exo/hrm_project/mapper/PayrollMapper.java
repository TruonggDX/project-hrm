package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.entity.PayrollEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PayrollMapper {

  ListPayrollDto toListDto(PayrollEntity entity);

  PayrollDto toDto(PayrollEntity entity);

  @Mapping(target = "employeeId", source = "employee.id")
  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "positionId", source = "position.id")
  PayrollEntity toEntity(PayrollDto dto);

  @Mapping(target = "employeeId", source = "employee.id")
  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "positionId", source = "position.id")
  void updatePayroll(PayrollDto dto, @MappingTarget PayrollEntity entity);
}
