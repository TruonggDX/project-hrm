package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IRewardPolicyService {

  BaseResponse<ResponsePage<ListRewardPolicyDto>> getAll(Pageable pageable,
      ListRewardPolicyDto filter);

  BaseResponse<DetailRewardPolicyDto> getRewardPolicy(Long id);
}
