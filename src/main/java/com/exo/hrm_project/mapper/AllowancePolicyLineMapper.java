package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyLineDto;
import com.exo.hrm_project.entity.AllowancePolicyLineEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AllowancePolicyLineMapper {

  @Mapping(source = "allowance.id", target = "allowanceId")
  AllowancePolicyLineEntity toEntity(AllowancePolicyLineDto allowancePolicyLineDto);
}
