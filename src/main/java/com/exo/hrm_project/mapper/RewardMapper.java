package com.exo.hrm_project.mapper;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.dto.detail_payroll.DetailsRewardDto;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardDto;
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

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "joinIncludeType")
  @Mapping(target = "groupRewardId", source = "groupReward.id")
  @Mapping(target = "uomId", source = "uom.id")
  @Mapping(target = "currencyId", source = "currency.id")
  RewardEntity toEntity(RewardDto dto);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "joinIncludeType")
  @Mapping(target = "groupRewardId", source = "groupReward.id")
  @Mapping(target = "uomId", source = "uom.id")
  @Mapping(target = "currencyId", source = "currency.id")
  void updateDto(RewardDto dto, @MappingTarget RewardEntity entity);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "splitIncludeType")
  DetailRewardDto toDetailDto(RewardEntity entity);

  @Mapping(target = "includeType", source = "includeType", qualifiedByName = "splitIncludeType")
  DetailsRewardDto toDetailsDto(RewardEntity entity);

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
