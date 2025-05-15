package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.group_allowance_reward.GroupAllowanceDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IGroupAllowanceService {

  BaseResponse<ResponsePage<ListGroupAllowanceDto>> getAllGroupAllowance(Pageable pageable,
      String code, String name, Boolean isActive);

  BaseResponse<ResponseCommon> createGroupAllowance(GroupAllowanceDto groupAllowanceDto);

  BaseResponse<ResponseCommon> updateGroupAllowance(GroupAllowanceDto groupAllowanceDto);

  BaseResponse<GroupAllowanceDto> getGroupAllowance(Long id);

  void deleteGroupAllowance(Long id);
}