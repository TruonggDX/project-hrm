package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.allowance_policy.AllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IAllowancePolicyService {

  BaseResponse<ResponsePage<ListAllowancePolicyDto>> getAll(Pageable pageable,
      ListAllowancePolicyDto filter);

  BaseResponse<ResponseCommon> createAllowancePolicy(AllowancePolicyDto allowancePolicyDto);

  BaseResponse<ResponseCommon> updateAllowancePolicy(AllowancePolicyDto allowancePolicyDto);

  BaseResponse<AllowancePolicyDto> getAllowancePolicy(Long id);

  void deleteAllowancePolicy(Long id);
}
