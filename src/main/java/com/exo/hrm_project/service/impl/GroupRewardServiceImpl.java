package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.group_allowance_reward.GroupRewardDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupRewardDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.GroupRewardEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.GroupRewardMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.GroupRewardRepository;
import com.exo.hrm_project.service.IGroupRewardService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.filter.FilterGroupReward;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupRewardServiceImpl implements IGroupRewardService {

  private final GroupRewardRepository repo;
  private final GroupRewardMapper groupRewardMapper;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListGroupRewardDto>> getAllGroupReward(Pageable pageable,
      String code, String name) {
    GenericSpecification<GroupRewardEntity> spec = FilterGroupReward.build(code, name);
    Page<GroupRewardEntity> page = repo.findAll(spec, pageable);
    return ResponseUtils.toPageResponse(page,
        page.getContent().stream().map(groupRewardMapper::toDtoList)
            .toList(), "Get All Group Reward");
  }

  @Override
  public BaseResponse<ResponseCommon> createGroupReward(GroupRewardDto groupRewardDto) {
    GroupRewardEntity groupRewardEntity = groupRewardMapper.toEntity(groupRewardDto);
    repo.save(groupRewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(groupRewardEntity),
        "Create GroupReward Successfully");
  }

  @Override
  public BaseResponse<ResponseCommon> updateGroupReward(GroupRewardDto groupRewardDto) {
    GroupRewardEntity groupRewardEntity = getGroupRewardById(groupRewardDto.getId());
    groupRewardMapper.updateGroupReward(groupRewardDto, groupRewardEntity);
    repo.save(groupRewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(groupRewardEntity),
        "Update GroupReward Successfully");
  }

  @Override
  public BaseResponse<GroupRewardDto> getGroupReward(Long id) {
    GroupRewardEntity groupRewardEntity = getGroupRewardById(id);
    GroupRewardDto dto = groupRewardMapper.toDto(groupRewardEntity);
    getParent(groupRewardEntity, dto);
    return BaseResponse.success(groupRewardMapper.toDto(groupRewardEntity),
        "Get GroupReward By Id Successfully");
  }

  @Override
  public void deleteGroupReward(Long id) {
    GroupRewardEntity groupRewardEntity = getGroupRewardById(id);
    repo.delete(groupRewardEntity);
  }

  private GroupRewardEntity getGroupRewardById(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found group reward id : " + id));
  }

  private void getParent(GroupRewardEntity entity, GroupRewardDto dto) {
    if (entity.getParentId() != null) {
      repo.findById(entity.getParentId())
          .ifPresent(parent -> dto.setParent(groupRewardMapper.toCommonDto(parent)));
    }
  }
}
