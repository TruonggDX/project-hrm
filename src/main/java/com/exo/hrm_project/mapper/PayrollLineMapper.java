package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.detail_payroll.AllowanceDtoLine;
import com.exo.hrm_project.dto.detail_payroll.DetailRewardDtoLine;
import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollLineDto;
import com.exo.hrm_project.dto.payroll.PayrolleLineDto;
import com.exo.hrm_project.entity.PayrollLineEntity;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PayrollLineMapper {

  PayrolleLineDto toDto(PayrollLineEntity entity);

  DetailRewardDtoLine toRewardDtoLine(PayrollLineEntity entity);

  AllowanceDtoLine toAllowanceDtoLine(PayrollLineEntity entity);

  PayrollLineEntity toEntity(AllowanceDtoLine allowanceDtoLine);

  PayrollLineEntity toEntity(CommonDto commonDto);


  PayrollLineEntity toEntity(PayrollDto formPayrollLineDto);

  void updatePayrollLine(FormPayrollLineDto formPayrollLineDto,
      @MappingTarget PayrollLineEntity entity);


  @Mapping(target = "payrollId", ignore = true)
  @Mapping(target = "type", constant = "ALLOWANCE")
  @Mapping(target = "groupTargetId", expression = "java(dto.getAllowance().getId())")
  @Mapping(target = "groupId", expression = "java(dto.getAllowance().getGroupAllowance().getId())")
  @Mapping(target = "amountItem", expression = "java(AmountItem.valueOf(dto.getAmountItem()))")
  PayrollLineEntity toAllowanceEntity(AllowanceDtoLine dto);

  @Mapping(target = "payrollId", ignore = true)
  @Mapping(target = "type", constant = "REWARD")
  @Mapping(target = "groupTargetId", expression = "java(dto.getReward().getId())")
  @Mapping(target = "groupId", expression = "java(dto.getReward().getGroupReward().getId())")
  @Mapping(target = "amountItem", expression = "java(AmountItem.valueOf(dto.getAmountItem()))")
  PayrollLineEntity toRewardEntity(DetailRewardDtoLine dto);

}
