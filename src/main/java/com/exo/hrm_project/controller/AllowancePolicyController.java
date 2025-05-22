package com.exo.hrm_project.controller;

import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.service.IAllowancePolicyService;
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
@RequestMapping("/api/v1/allowance-policy")
public class AllowancePolicyController {

  private final IAllowancePolicyService iAllowancePolicyService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListAllowancePolicyDto>>> getAllowancePolicyList(
      Pageable pageable, @ParameterObject ListAllowancePolicyDto filter) {
    BaseResponse<ResponsePage<ListAllowancePolicyDto>> response = iAllowancePolicyService.getAll(
        pageable, filter);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<BaseResponse<ResponseCommon>> addAllowancePolicy(
      @RequestBody DetailAllowancePolicyDto allowancePolicyDto) {
    BaseResponse<ResponseCommon> response = iAllowancePolicyService.createAllowancePolicy(
        allowancePolicyDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateAllowance(
     @RequestBody DetailAllowancePolicyDto allowancePolicyDto) {
    BaseResponse<ResponseCommon> response = iAllowancePolicyService.updateAllowancePolicy(
        allowancePolicyDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<DetailAllowancePolicyDto>> getAllowancePolicyById(
      @RequestParam Long id) {
    BaseResponse<DetailAllowancePolicyDto> response = iAllowancePolicyService.getAllowancePolicy(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deleteAllowancePolicyById(@RequestParam(name = "id") Long id) {
    iAllowancePolicyService.deleteAllowancePolicy(id);
  }
}
