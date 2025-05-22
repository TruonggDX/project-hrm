package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.common.FilterRequest;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IRewardPolicyService {

  BaseResponse<ResponsePage<ListRewardPolicyDto>> getAll(Pageable pageable,
      FilterRequest filter);

  BaseResponse<ResponseCommon> createRewardPolicy(DetailRewardPolicyDto rewardPolicyDto);

  BaseResponse<ResponseCommon> updateRewardPolicy(DetailRewardPolicyDto rewardPolicyDto);

  BaseResponse<DetailRewardPolicyDto> getRewardPolicy(Long id);

  void deleteRewardPolicy(Long id);

}
