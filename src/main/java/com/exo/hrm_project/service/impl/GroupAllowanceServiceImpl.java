package com.exo.hrm_project.service.impl;

import com.exo.hrm_project.dto.group_allowance_reward.GroupAllowanceDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.entity.GroupAllowanceEntity;
import com.exo.hrm_project.exception.NotFoundException;
import com.exo.hrm_project.mapper.GroupAllowanceMapper;
import com.exo.hrm_project.mapper.common.GenericIdMapper;
import com.exo.hrm_project.repository.GroupAllowanceRepository;
import com.exo.hrm_project.service.IGroupAllowanceService;
import com.exo.hrm_project.specification.GenericSpecification;
import com.exo.hrm_project.specification.filter.FilterGroupAllowance;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import com.exo.hrm_project.utils.response.ResponseUtil;
import com.exo.hrm_project.utils.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GroupAllowanceServiceImpl implements IGroupAllowanceService {

  private final GroupAllowanceRepository repo;
  private final GroupAllowanceMapper mapper;
  private final GenericIdMapper genericIdMapper;
  private final ResponseUtil responseUtil;


  @Override
  public BaseResponse<ResponsePage<ListGroupAllowanceDto>> getAllGroupAllowance(Pageable pageable,
      String code, String name, Boolean isActive) {
    GenericSpecification<GroupAllowanceEntity> spec = FilterGroupAllowance.build(code, name,
        isActive);
    Page<GroupAllowanceEntity> page = repo.findAll(spec, pageable);
    return ResponseUtils.toPageResponse(page,
        page.getContent().stream().map(mapper::toDtoList).toList(),
        "Get All Group Allowance Policy");
  }

  @Override
  public BaseResponse<ResponseCommon> createGroupAllowance(GroupAllowanceDto groupAllowanceDto) {
    GroupAllowanceEntity groupAllowanceEntity = mapper.toEntity(groupAllowanceDto);
    groupAllowanceEntity = repo.save(groupAllowanceEntity);
    return responseUtil.success(genericIdMapper.toResponseCommon(groupAllowanceEntity),
        "response.success");

  }

  @Override
  public BaseResponse<ResponseCommon> updateGroupAllowance(GroupAllowanceDto groupAllowanceDto) {
    GroupAllowanceEntity groupAllowance = getGroupAllowanceById(groupAllowanceDto.getId());
    mapper.updateGroupAllowance(groupAllowanceDto, groupAllowance);
    groupAllowance = repo.save(groupAllowance);
    return responseUtil.success(genericIdMapper.toResponseCommon(groupAllowance),
        "response.success");

  }

  @Override
  public BaseResponse<GroupAllowanceDto> getGroupAllowance(Long id) {
    GroupAllowanceEntity entity = getGroupAllowanceById(id);
    GroupAllowanceDto dto = mapper.toDto(entity);
    getParent(entity, dto);
    return responseUtil.success(dto, "response.success");
  }

  @Override
  public void deleteGroupAllowance(Long id) {
    GroupAllowanceEntity groupAllowanceEntity = repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Not Found group allowance id : " + id));
    repo.delete(groupAllowanceEntity);
  }

  private GroupAllowanceEntity getGroupAllowanceById(Long id) {
    return repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Not Found group allowance id : " + id));
  }

  private void getParent(GroupAllowanceEntity entity, GroupAllowanceDto dto) {
    if (entity.getParentId() != null) {
      repo.findById(entity.getParentId())
          .ifPresent(parent -> dto.setParent(mapper.toCommonDto(parent)));
    }
  }


}
