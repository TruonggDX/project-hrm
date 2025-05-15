package com.exo.hrm_project.controller;


import com.exo.hrm_project.dto.group_allowance_reward.GroupAllowanceDto;
import com.exo.hrm_project.dto.group_allowance_reward.ListGroupAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.service.IGroupAllowanceService;
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
@RequestMapping("/api/v1/group-allowance")
public class GroupAllowanceController {

  private final IGroupAllowanceService iGroupAllowanceService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListGroupAllowanceDto>>> getGroupAllowance(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Boolean isActive,
      Pageable pageable) {
    BaseResponse<ResponsePage<ListGroupAllowanceDto>> responsePage = iGroupAllowanceService.getAllGroupAllowance(
        pageable, code, name, isActive);
    return ResponseEntity.ok(responsePage);
  }

  @PostMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> createGroupAllowance(
  @RequestBody GroupAllowanceDto groupAllowanceDto) {
    BaseResponse<ResponseCommon> response = iGroupAllowanceService.createGroupAllowance(
        groupAllowanceDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<GroupAllowanceDto>> getGroupAllowances(
      @RequestParam(name = "id") Long id) {
    BaseResponse<GroupAllowanceDto> response = iGroupAllowanceService.getGroupAllowance(id);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateGroupAllowance(
      @RequestBody GroupAllowanceDto groupAllowanceDto) {
    BaseResponse<ResponseCommon> response = iGroupAllowanceService.updateGroupAllowance(
        groupAllowanceDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deleteGroupAllowance(@RequestParam(name = "id") Long id) {
    iGroupAllowanceService.deleteGroupAllowance(id);
  }
}
