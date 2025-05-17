package com.exo.hrm_project.controller;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward_policy.DetailRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.ListRewardPolicyDto;
import com.exo.hrm_project.dto.reward_policy.RewardPolicyDto;
import com.exo.hrm_project.service.IRewardPolicyService;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

  @PostMapping
  public ResponseEntity<BaseResponse<ResponseCommon>> addAllowancePolicy(
      @RequestBody RewardPolicyDto rewardPolicyDto) {
    BaseResponse<ResponseCommon> response = iRewardPolicyService.createRewardPolicy(
        rewardPolicyDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateAllowance(
      @RequestBody RewardPolicyDto rewardPolicyDto) {
    BaseResponse<ResponseCommon> response = iRewardPolicyService.updateRewardPolicy(
        rewardPolicyDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<DetailRewardPolicyDto>> getRewardPolicyById(
      @RequestParam Long id) {
    BaseResponse<DetailRewardPolicyDto> response = iRewardPolicyService.getRewardPolicy(
        id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deleteRewardPolicyById(@RequestParam(name = "id") Long id) {
    iRewardPolicyService.deleteRewardPolicy(id);
  }
}
