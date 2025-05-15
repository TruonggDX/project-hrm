package com.exo.hrm_project.controller;

import com.exo.hrm_project.dto.group_allowance_reward.GroupRewardDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupRewardDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.service.IGroupRewardService;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
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
@RequestMapping("/api/v1/group-reward")
public class GroupRewardController {

  private final IGroupRewardService iGroupRewardService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListGroupRewardDto>>> getGroupReward(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name,
      Pageable pageable) {
    BaseResponse<ResponsePage<ListGroupRewardDto>> responsePage = iGroupRewardService.getAllGroupReward(
        pageable, code, name);
    return ResponseEntity.ok(responsePage);
  }

  @PostMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> createGroupReward(
      @RequestBody GroupRewardDto groupRewardDto) {
    BaseResponse<ResponseCommon> response = iGroupRewardService.createGroupReward(
        groupRewardDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateGroupReward(
      @RequestBody GroupRewardDto groupRewardDto) {
    BaseResponse<ResponseCommon> response = iGroupRewardService.updateGroupReward(
        groupRewardDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<GroupRewardDto>> getGroupReward(
      @RequestParam(name = "id") Long id) {
    BaseResponse<GroupRewardDto> response = iGroupRewardService.getGroupReward(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deleteGroupReward(@RequestParam(name = "id") Long id) {
    iGroupRewardService.deleteGroupReward(id);
  }
}
