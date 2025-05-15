package com.exo.hrm_project.service;


import com.exo.hrm_project.dto.group_allowance_reward.GroupRewardDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupRewardDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IGroupRewardService {

  BaseResponse<ResponsePage<ListGroupRewardDto>> getAllGroupReward(Pageable pageable, String code,
      String name);

  BaseResponse<ResponseCommon> createGroupReward(GroupRewardDto groupRewardDto);

  BaseResponse<ResponseCommon> updateGroupReward(GroupRewardDto groupRewardDto);

  BaseResponse<GroupRewardDto> getGroupReward(Long id);

  void deleteGroupReward(Long id);
}
