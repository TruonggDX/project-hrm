package com.exo.hrm_project.controller;

import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.service.IPayrolleService;
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
@RequestMapping("/api/v1/payroll")
public class PayrollController {

  private final IPayrolleService iPayrolleService;

  @GetMapping("/list")
  public ResponseEntity<BaseResponse<ResponsePage<ListPayrollDto>>> getListPayroll(
      Pageable pageable) {
    BaseResponse<ResponsePage<ListPayrollDto>> response = iPayrolleService.getAll(pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping()
  public ResponseEntity<BaseResponse<PayrollDto>> getAllowances(
      @RequestParam(name = "id") Long id) {
    BaseResponse<PayrollDto> response = iPayrolleService.getPayrollById(id);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<BaseResponse<ResponseCommon>> createAllowance(
      @RequestBody FormPayrollDto formPayrollDto) {
    BaseResponse<ResponseCommon> response = iPayrolleService.createPayroll(formPayrollDto);
    return ResponseEntity.ok(response);
  }

  @PutMapping()
  public ResponseEntity<BaseResponse<ResponseCommon>> updateAllowance(
      @RequestBody FormPayrollDto formPayrollDto) {
    BaseResponse<ResponseCommon> response = iPayrolleService.updatePayroll(formPayrollDto);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping()
  public void deletePayroll(@RequestParam(name = "id") Long id) {
    iPayrolleService.deletePayrollById(id);
  }
}
