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
import com.exo.hrm_project.specification.SearchCriteria;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtils;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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
    GenericSpecification<GroupRewardEntity> spec = new GenericSpecification<>();
    if (code != null && !code.isEmpty()) {
      spec.add(new SearchCriteria("code", ":", code));
    }
    if (name != null && !name.isEmpty()) {
      spec.add(new SearchCriteria("name", ":", name));
    }
    Page<GroupRewardEntity> page = repo.findAll(spec, pageable);
    List<ListGroupRewardDto> dtos = page.getContent().stream().map(groupRewardMapper::toDtoList)
        .toList();
    return ResponseUtils.toPageResponse(page, dtos, "Get All Group Reward");
  }

  @Override
  public BaseResponse<ResponseCommon> createGroupReward(GroupRewardDto groupRewardDto) {
    GroupRewardEntity groupRewardEntity = groupRewardMapper.toEntity(groupRewardDto);
    if (groupRewardDto.getParent() != null && groupRewardDto.getParent().getId() != null) {
      groupRewardEntity.setParentId(groupRewardDto.getParent().getId());
    }
    repo.save(groupRewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(groupRewardEntity),
        "Create GroupReward Successfully");
  }

  @Override
  public BaseResponse<ResponseCommon> updateGroupReward(GroupRewardDto groupRewardDto) {
    Optional<GroupRewardEntity> check = repo.findById(groupRewardDto.getId());
    if (check.isEmpty()) {
      return BaseResponse.notFound("Not Found id : " + groupRewardDto.getId());
    }
    GroupRewardEntity groupRewardEntity = check.get();
    if (groupRewardDto.getParent() != null && groupRewardDto.getParent().getId() != null) {
      groupRewardEntity.setParentId(groupRewardDto.getParent().getId());
    }
    groupRewardMapper.updateGroupReward(groupRewardDto, groupRewardEntity);
    repo.save(groupRewardEntity);
    return BaseResponse.success(genericIdMapper.toResponseCommon(groupRewardEntity),
        "Update GroupReward Successfully");
  }

  @Override
  public BaseResponse<GroupRewardDto> getGroupReward(Long id) {
    BaseResponse<GroupRewardDto> response = new BaseResponse<>();
    Optional<GroupRewardEntity> check = repo.findById(id);
    if (check.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage("Not Found id : " + id);
      return response;
    }
    return BaseResponse.success(groupRewardMapper.toDto(check.get()),
        "Get GroupReward By Id Successfully");
  }

  @Override
  public void deleteGroupReward(Long id) {
    GroupRewardEntity groupRewardEntity = repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Not found group reward id : " + id));
    repo.delete(groupRewardEntity);
  }
}
