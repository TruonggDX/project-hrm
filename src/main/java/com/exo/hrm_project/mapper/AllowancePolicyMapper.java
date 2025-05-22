package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.entity.AllowancePolicyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AllowancePolicyMapper {

  ListAllowancePolicyDto toListDto(AllowancePolicyEntity entity);

  AllowancePolicyEntity toEntity(DetailAllowancePolicyDto dto);

  DetailAllowancePolicyDto toDto(AllowancePolicyEntity entity);

  void updateAllowancePolicy(DetailAllowancePolicyDto dto, @MappingTarget AllowancePolicyEntity entity);
}
