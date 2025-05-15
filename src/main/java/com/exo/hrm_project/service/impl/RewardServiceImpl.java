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
import com.exo.hrm_project.service.ICurrencyService;
import com.exo.hrm_project.service.IRewardService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.SearchCriteria;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RewardServiceImpl implements IRewardService {

  private final RewardRepository rewardRepository;
  private final RewardMapper rewardMapper;
  private final GroupRewardRepository groupRewardRepository;
  private final GenericIdMapper genericIdMapper;
  private final UomServiceImpl uomServiceImpl;
  private final ICurrencyService iCurrencyService;

  @Override
  public BaseResponse<ResponsePage<ListRewardDto>> getAll(Pageable pageable, String code,
      String name) {
    BaseResponse<ResponsePage<ListRewardDto>> response = new BaseResponse<>();
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
    ResponsePage<ListRewardDto> responsePage = new ResponsePage<>();
    responsePage.setPageNumber(pageable.getPageNumber());
    responsePage.setPageSize(pageable.getPageSize());
    responsePage.setTotalElements(page.getTotalElements());
    responsePage.setTotalPages(page.getTotalPages());
    responsePage.setContent(rewardDtos);
    responsePage.setNumberOfElements(page.getNumberOfElements());
    responsePage.setSort(page.getSort().toString());
    response.setData(responsePage);
    response.setMessage("Get All Reward Policy");
    response.setCode(HttpStatus.OK.value());
    return response;
  }

  @Override
  public BaseResponse<ResponseCommon> createReward(RewardDto rewardDto) {
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    Optional<GroupRewardEntity> check = groupRewardRepository.findById(
        rewardDto.getGroupReward().getId());
    if (check.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage(
          "Not found group allowance id: " + rewardDto.getGroupReward().getId());
      return response;
    }
    RewardEntity rewardEntity = rewardMapper.toEntity(rewardDto);
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
    rewardRepository.save(rewardEntity);
    response.setCode(HttpStatus.CREATED.value());
    response.setMessage("Create reward successfully");
    response.setData(genericIdMapper.toResponseCommon(rewardEntity));
    return response;
  }

  @Override
  public BaseResponse<ResponseCommon> updateReward(RewardDto rewardDto) {
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    Optional<RewardEntity> checkRewardId = rewardRepository.findById(rewardDto.getId());
    if (checkRewardId.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage("Not found reward id: " + rewardDto.getId());
      return response;
    }
    Optional<GroupRewardEntity> check = groupRewardRepository.findById(
        rewardDto.getGroupReward().getId());
    if (check.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage(
          "Not found group reward id: " + rewardDto.getGroupReward().getId());
      return response;
    }
    RewardEntity rewardEntity = checkRewardId.get();
    rewardMapper.updateDto(rewardDto, rewardEntity);
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
    rewardRepository.save(rewardEntity);
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Update Allowance successfully");
    response.setData(genericIdMapper.toResponseCommon(rewardEntity));
    return response;
  }

  @Override
  public BaseResponse<RewardDto> getRewardById(Long id) {
    BaseResponse<RewardDto> response = new BaseResponse<>();
    RewardEntity rewardEntity = rewardRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance id : " + id));
    RewardDto dto = rewardMapper.toAddDto(rewardEntity);
    dto.setUom(uomServiceImpl.getUomById(rewardEntity.getUomId()));
    dto.setCurrency(iCurrencyService.getCurrencyById(rewardEntity.getCurrencyId()));
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Update Allowance successfully");
    response.setData(dto);
    return response;
  }

  @Override
  public void deleteReward(Long id) {
    RewardEntity rewardEntity = rewardRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found allowance id : " + id));
    rewardRepository.delete(rewardEntity);
  }
}
