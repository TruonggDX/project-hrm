package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.utils.enums.DeductionType;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RewardMapper {

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "splitIncludeType")
  RewardDto toDto(RewardEntity entity);

  ListRewardDto toListDto(RewardEntity entity);

  CommonDto toCommonDto(GroupRewardEntity entity);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "splitIncludeType")
  @Mapping(source = "uomId", target = "uom.id")
  @Mapping(source = "currencyId", target = "currency.id")
  RewardDto toAddDto(RewardEntity entity);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "joinIncludeType")
  RewardEntity toEntity(RewardDto dto);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "joinIncludeType")
  void updateDto(RewardDto dto, @MappingTarget RewardEntity entity);

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
