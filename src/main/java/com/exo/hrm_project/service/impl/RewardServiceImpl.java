package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.entity.RewardEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.RewardMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.repository.RewardRepository;
import com.exo.hrm_project.service.IExternalService;
import com.exo.hrm_project.service.IRewardService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.filter.FilterGroupReward;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RewardServiceImpl implements IRewardService {

  private final RewardRepository rewardRepository;
  private final GroupRewardRepository groupRewardRepository;
  private final RewardMapper rewardMapper;
  private final GenericIdMapper genericIdMapper;
  private final IExternalService iExternalService;

  @Override
  public BaseResponse<ResponsePage<ListRewardDto>> getAll(Pageable pageable, String code,
      String name) {
    GenericSpecification<RewardEntity> spec = FilterGroupReward.build(code, name);
    Page<RewardEntity> page = rewardRepository.findAll(spec, pageable);
    List<ListRewardDto> rewardDto = page.getContent().stream()
        .map(rewardMapper::toListDto)
        .toList();
    return ResponseUtils.toPageResponse(page, rewardDto, "Get All Reward");
  }

  @Override
  public BaseResponse<ResponseCommon> createReward(RewardDto rewardDto) {
    RewardEntity rewardEntity = rewardMapper.toEntity(rewardDto);
    rewardRepository.save(rewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(rewardEntity),
        "Create reward successfully");
  }

  @Override
  public BaseResponse<ResponseCommon> updateReward(RewardDto rewardDto) {
    RewardEntity rewardEntity = getById(rewardDto.getId());
    rewardMapper.updateDto(rewardDto, rewardEntity);
    rewardRepository.save(rewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(rewardEntity),
        "Update reward successfully");
  }

  @Override
  public BaseResponse<RewardDto> getRewardById(Long id) {
    RewardEntity rewardEntity = getById(id);
    return BaseResponse.success(getDataExternal(rewardEntity), "Get reward successfully");
  }

  @Override
  @Transactional
  public void deleteReward(Long id) {
    RewardEntity rewardEntity = rewardRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward id : " + id));
    rewardRepository.delete(rewardEntity);
  }

  public RewardDto getDataExternal(RewardEntity entity) {
    RewardDto dto = rewardMapper.toDto(entity);
    if (entity.getGroupRewardId() != null) {
      groupRewardRepository.findById(entity.getGroupRewardId())
          .ifPresent(parent -> dto.setGroupReward(rewardMapper.toCommonDto(parent)));
    }
    dto.setUom(iExternalService.getUomById(entity.getUomId()));
    dto.setCurrency(iExternalService.getCurrencyById(entity.getCurrencyId()));
    return dto;
  }

  private RewardEntity getById(Long id) {
    return rewardRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found reward id : " + id));
  }
}
