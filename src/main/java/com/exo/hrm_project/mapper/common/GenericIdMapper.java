package com.exo.hrm_project.mapper.common;

import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.BaseEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GenericIdMapper {

  @Mapping(target = "id", source = "id")
  ResponseCommon toResponseCommon(BaseEntity entity);
}
