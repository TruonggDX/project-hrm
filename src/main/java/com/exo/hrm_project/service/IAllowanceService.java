package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.allowance.AllowanceDto;
import com.exo.hrm_project.dto.allowance.ListAllowanceDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IAllowanceService {

  BaseResponse<ResponsePage<ListAllowanceDto>> getAll(Pageable pageable, String code, String name,
      Boolean isActive);

  BaseResponse<ResponseCommon> createAllowance(AllowanceDto allowanceDto);

  BaseResponse<ResponseCommon> updateAllowance(AllowanceDto allowanceDto);

  BaseResponse<AllowanceDto> getAllowanceById(Long id);

  void deleteAllowance(Long id);

}
