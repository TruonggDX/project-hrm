package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.entity.AllowancePolicyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AllowancePolicyMapper {

  ListAllowancePolicyDto toListDto(AllowancePolicyEntity entity);

  AllowancePolicyEntity toEntity(AllowancePolicyDto dto);
}
