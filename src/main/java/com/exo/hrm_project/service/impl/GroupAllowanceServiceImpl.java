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
public class GroupAllowanceServiceImpl implements IGroupAllowanceService {

  private final GroupAllowanceRepository repo;
  private final GroupAllowanceMapper mapper;
  private final GenericIdMapper genericIdMapper;

  @Override
  public BaseResponse<ResponsePage<ListGroupAllowanceDto>> getAllGroupAllowance(Pageable pageable, String code, String name, Boolean isActive) {
    GenericSpecification<GroupAllowanceEntity> spec = new GenericSpecification<>();
    if (code != null && !code.isEmpty()) {
      spec.add(new SearchCriteria("code", ":", code));
    }
    if (name != null && !name.isEmpty()) {
      spec.add(new SearchCriteria("name", ":", name));
    }
    if (isActive != null) {
      spec.add(new SearchCriteria("isActive", "=", isActive));
    }

    Page<GroupAllowanceEntity> page = repo.findAll(spec, pageable);
    List<ListGroupAllowanceDto> dtos = page.getContent().stream().map(mapper::toDtoList).toList();
    return ResponseUtils.toPageResponse(page, dtos, "Get All Group Allowance Policy");
  }


  @Override
  public BaseResponse<ResponseCommon> createGroupAllowance(GroupAllowanceDto groupAllowanceDto) {
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    GroupAllowanceEntity groupAllowanceEntity = mapper.toEntity(groupAllowanceDto);
    if (groupAllowanceDto.getParent() != null && groupAllowanceDto.getParent().getId() != null) {
      groupAllowanceEntity.setParentId(groupAllowanceDto.getParent().getId());
    }
    groupAllowanceEntity = repo.save(groupAllowanceEntity);
    response.setData(genericIdMapper.toResponseCommon(groupAllowanceEntity));
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Created GroupAllowance Successfully");
    return response;
  }

  @Override
  public BaseResponse<ResponseCommon> updateGroupAllowance(GroupAllowanceDto groupAllowanceDto) {
    BaseResponse<ResponseCommon> response = new BaseResponse<>();
    Optional<GroupAllowanceEntity> groupAllowanceEntity = repo.findById(groupAllowanceDto.getId());
    if (groupAllowanceEntity.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage("Not Found id of groupAllowanceId : " + groupAllowanceDto.getId());
      return response;
    }
    GroupAllowanceEntity groupAllowance = groupAllowanceEntity.get();
    if (groupAllowanceDto.getParent() != null && groupAllowanceDto.getParent().getId() != null) {
      groupAllowance.setParentId(groupAllowanceDto.getParent().getId());
    }
    mapper.updateGroupAllowance(groupAllowanceDto, groupAllowance);
    groupAllowance = repo.save(groupAllowance);
    response.setData(genericIdMapper.toResponseCommon(groupAllowance));
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Updated GroupAllowance Successfully");
    return response;
  }

  @Override
  public BaseResponse<GroupAllowanceDto> getGroupAllowance(Long id) {
    BaseResponse<GroupAllowanceDto> response = new BaseResponse<>();
    Optional<GroupAllowanceEntity> check = repo.findById(id);
    if (check.isEmpty()) {
      response.setCode(HttpStatus.NOT_FOUND.value());
      response.setMessage("Not Found group allowace id : " + id);
      return response;
    }
    response.setData(mapper.toDto(check.get()));
    response.setCode(HttpStatus.OK.value());
    response.setMessage("Get GroupAllowance Successfully");
    return response;
  }

  @Override
  public void deleteGroupAllowance(Long id) {
    GroupAllowanceEntity groupAllowanceEntity = repo.findById(id)
        .orElseThrow(() -> new NotFoundException("Not Found group allowance id : " + id));
    repo.delete(groupAllowanceEntity);
  }

}
