package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.detail_payroll.AllowanceDtoLine;
import com.exo.hrm_project.dto.detail_payroll.DetailRewardDtoLine;
import com.exo.hrm_project.dto.payroll.FormPayrollLineDto;
import com.exo.hrm_project.dto.payroll.PayrolleLineDto;
import com.exo.hrm_project.entity.PayrollLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PayrollLineMapper {

  PayrolleLineDto toDto(PayrollLineEntity entity);

  DetailRewardDtoLine toRewardDtoLine(PayrollLineEntity entity);

  AllowanceDtoLine toAllowanceDtoLine(PayrollLineEntity entity);

  PayrollLineEntity toEntity(FormPayrollLineDto formPayrollLineDto);

  void updatePayrollLine(FormPayrollLineDto formPayrollLineDto,
      @MappingTarget PayrollLineEntity entity);
}
