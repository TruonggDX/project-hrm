package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.repository.RewardRepository;
import com.exo.hrm_project.service.IRewardService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RewardServiceImpl implements IRewardService {

  private final RewardRepository rewardRepository;
  private final RewardMapper rewardMapper;
  private final GroupRewardRepository groupRewardRepository;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListRewardDto>> getAll(Pageable pageable, String code,
      String name) {
    GenericSpecification<RewardEntity> spec = new GenericSpecification<>();
    if (code != null && !code.isEmpty()) {
      spec.add(new SearchCriteria("code", ":", code));
    }
    if (name != null && !name.isEmpty()) {
      spec.add(new SearchCriteria("name", ":", name));
    }
    Page<RewardEntity> page = rewardRepository.findAll(spec, pageable);
    List<ListRewardDto> rewardDtos = page.getContent().stream()
        .map(rewardMapper::toListDto)
        .toList();
    return ResponseUtils.toPageResponse(page, rewardDtos, "Get All Reward");
  }

  @Override
  public BaseResponse<ResponseCommon> createReward(RewardDto rewardDto) {
    Optional<GroupRewardEntity> check = groupRewardRepository.findById(
        rewardDto.getGroupReward().getId());
    if (check.isEmpty()) {
      return BaseResponse.notFound(
          "Not found group reward id: " + rewardDto.getGroupReward().getId());
    }
    RewardEntity rewardEntity = rewardMapper.toEntity(rewardDto);
    mapRewardDtoToEntity(rewardDto, rewardEntity);
    rewardRepository.save(rewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(rewardEntity),
        "Create reward successfully");
  }

  @Override
  public BaseResponse<ResponseCommon> updateReward(RewardDto rewardDto) {
    Optional<RewardEntity> checkRewardId = rewardRepository.findById(rewardDto.getId());
    if (checkRewardId.isEmpty()) {
      return BaseResponse.notFound("Not found reward id: " + rewardDto.getId());
    }
    Optional<GroupRewardEntity> check = groupRewardRepository.findById(
        rewardDto.getGroupReward().getId());
    if (check.isEmpty()) {
      return BaseResponse.notFound(
          "Not found group reward id: " + rewardDto.getGroupReward().getId());
    }
    RewardEntity rewardEntity = checkRewardId.get();
    rewardMapper.updateDto(rewardDto, rewardEntity);
    mapRewardDtoToEntity(rewardDto, rewardEntity);
    rewardRepository.save(rewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(rewardEntity),
        "Update reward successfully");
  }

  private void mapRewardDtoToEntity(RewardDto rewardDto, RewardEntity rewardEntity) {
    if (rewardDto.getGroupReward() != null
        && rewardDto.getGroupReward().getId() != null) {
      rewardEntity.setGroupRewardId(rewardDto.getGroupReward().getId());
    }
    if (rewardDto.getUom() != null && rewardDto.getUom().getId() != null) {
      rewardEntity.setUomId(rewardDto.getUom().getId());
    }
    if (rewardDto.getCurrency() != null && rewardDto.getCurrency().getId() != null) {
      rewardEntity.setCurrencyId(rewardDto.getCurrency().getId());
    }
  }

  @Override
  public BaseResponse<RewardDto> getRewardById(Long id) {
    RewardEntity rewardEntity = rewardRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance id : " + id));
    RewardDto dto = rewardMapper.toDto(rewardEntity);
    return BaseResponse.success(dto, "Get reward successfully");
  }

  @Override
  @Transactional
  public void deleteReward(Long id) {
    RewardEntity rewardEntity = rewardRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward id : " + id));
    rewardRepository.delete(rewardEntity);
  }
}
