package com.exo.hrm_project.controller;


import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance.ListAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.service.IAllowanceService;
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
@RequestMapping("/api/v1/allowance")
public class AllowanceController {

  private final IAllowanceService iAllowanceService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListAllowanceDto>>> getAllowance(
      @RequestParam(required = false) String code,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Boolean isActive,
      Pageable pageable) {
    BaseResponse<ResponsePage<ListAllowanceDto>> responsePage = iAllowanceService.getAll(
        pageable, code, name, isActive);
    return ResponseEntity.ok(responsePage);
  }

  @PostMapping
  public ResponseEntity<BaseResponse<ResponseCommon>> createAllowance(
      @RequestBody AllowanceDto allowanceDto) {
    BaseResponse<ResponseCommon> response = iAllowanceService.createAllowance(allowanceDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateAllowance(
      @Valid @RequestBody AllowanceDto allowanceDto) {
    BaseResponse<ResponseCommon> response = iAllowanceService.updateAllowance(
        allowanceDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<AllowanceDto>> getAllowances(
      @RequestParam(name = "id") Long id) {
    BaseResponse<AllowanceDto> response = iAllowanceService.getAllowanceById(id);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deleteAllowance(@RequestParam(name = "id") Long id) {
    iAllowanceService.deleteAllowance(id);
  }
}
