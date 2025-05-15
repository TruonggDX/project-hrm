package com.exo.hrm_project.mapper;


import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance.ListAllowanceDto;
import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.entity.AllowanceEntity;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.utils.enums.DeductionType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AllowanceMapper {

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "splitIncludeType")
  AllowanceDto toDto(AllowanceEntity entity);

  ListAllowanceDto toListDto(AllowanceEntity entity);

  CommonDto toCommonDto(GroupAllowanceEntity entity);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "splitIncludeType")
  @Mapping(source = "uomId", target = "uom.id")
  @Mapping(source = "currencyId", target = "currency.id")
  AllowanceDto toAddDto(AllowanceEntity entity);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "joinIncludeType")
  AllowanceEntity toEntity(AllowanceDto dto);


  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "joinIncludeType")
  void updateDto(AllowanceDto dto, @MappingTarget AllowanceEntity entity);

  @Named("splitIncludeType")
  default List<DeductionType> splitIncludeType(String includeType) {
    return Arrays.stream(includeType.split(","))
        .map(String::trim)
        .map(DeductionType::valueOf)
        .toList();
  }

  @Named("joinIncludeType")
  default String joinIncludeType(List<DeductionType> includeTypes) {
    return includeTypes.stream()
        .map(Enum::name)
        .collect(Collectors.joining(","));
  }
}
