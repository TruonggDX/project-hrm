package com.exo.hrm_project.controller;

import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.dto.reward.ListRewardDto;
import com.exo.hrm_project.dto.reward.RewardDto;
import com.exo.hrm_project.service.IRewardService;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/reward")
public class RewardController {

  private final IRewardService iRewardService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListRewardDto>>> getReward(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name,
      Pageable pageable) {
    BaseResponse<ResponsePage<ListRewardDto>> responsePage = iRewardService.getAll(
        pageable, code, name);
    return ResponseEntity.ok(responsePage);
  }

  @PostMapping
  public ResponseEntity<BaseResponse<ResponseCommon>> createReward(
      @RequestBody RewardDto rewardDto) {
    BaseResponse<ResponseCommon> response = iRewardService.createReward(rewardDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateReward(
      @Valid @RequestBody RewardDto rewardDto) {
    BaseResponse<ResponseCommon> response = iRewardService.updateReward(
        rewardDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<RewardDto>> getRewards(
      @RequestParam(name = "id") Long id) {
    BaseResponse<RewardDto> response = iRewardService.getRewardById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deleteReward(@RequestParam(name = "id") Long id) {
    iRewardService.deleteReward(id);
  }
}
