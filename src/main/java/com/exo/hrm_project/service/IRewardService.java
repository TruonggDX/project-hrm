package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IRewardService {

  BaseResponse<ResponsePage<ListRewardDto>> getAll(Pageable pageable, String code, String name);

  BaseResponse<ResponseCommon> createReward(RewardDto rewardDto);

  BaseResponse<ResponseCommon> updateReward(RewardDto rewardDto);

  BaseResponse<RewardDto> getRewardById(Long id);

  void deleteReward(Long id);
}
