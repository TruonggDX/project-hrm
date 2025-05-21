package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.detail_payroll.PayrollDto;
import com.exo.hrm_project.dto.payroll.FormPayrollDto;
import com.exo.hrm_project.dto.payroll.ListPayrollDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IPayrolleService {

  BaseResponse<ResponsePage<ListPayrollDto>> getAll(Pageable pageable);

  BaseResponse<ResponseCommon> createPayroll(FormPayrollDto formPayrollDto);

  BaseResponse<ResponseCommon> updatePayroll(FormPayrollDto formPayrollDto);

  BaseResponse<PayrollDto> getPayrollById(Long id);

  void deletePayrollById(Long id);
}
