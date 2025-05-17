package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyLineDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyLineDto;
import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AllowancePolicyLineMapper {

  @Mapping(source = "allowance.id", target = "allowanceId")
  AllowancePolicyLineEntity toEntity(AllowancePolicyLineDto allowancePolicyLineDto);

  DetailAllowancePolicyLineDto toDto(AllowancePolicyLineEntity allowancePolicyLineEntity);

  AllowancePolicyLineDto toPolicyLineDto(AllowancePolicyLineEntity allowancePolicyLine);

  void updatePolicyLine(AllowancePolicyLineDto allowancePolicyLineDto,
      @MappingTarget AllowancePolicyLineEntity allowancePolicyLineEntity);
}
