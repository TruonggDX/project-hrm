package com.exo.hrm_project.controller;

import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.service.IRewardPolicyService;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reward-policy")
public class RewardPolicyController {

  private final IRewardPolicyService iRewardPolicyService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListRewardPolicyDto>>> getRewardPolicyList(
      Pageable pageable, @ParameterObject ListRewardPolicyDto filter) {
    BaseResponse<ResponsePage<ListRewardPolicyDto>> response = iRewardPolicyService.getAll(
        pageable, filter);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<DetailRewardPolicyDto>> getAllowancePolicyById(
      @RequestParam Long id) {
    BaseResponse<DetailRewardPolicyDto> response = iRewardPolicyService.getRewardPolicy(
        id);
    return ResponseEntity.ok(response);
  }
}
