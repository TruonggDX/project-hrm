package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.allowance_policy.DetailAllowancePolicyDto;
import com.exo.hrm_project.dto.allowance_policy.ListAllowancePolicyDto;
import com.exo.hrm_project.dto.common.FilterRequest;
import com.exo.hrm_project.dto.response.ResponseCommon;
import com.exo.hrm_project.utils.response.BaseResponse;
import com.exo.hrm_project.utils.response.ResponsePage;
import org.springframework.data.domain.Pageable;

public interface IAllowancePolicyService {

  BaseResponse<ResponsePage<ListAllowancePolicyDto>> getAll(Pageable pageable,
      FilterRequest filter);

  BaseResponse<ResponseCommon> createAllowancePolicy(DetailAllowancePolicyDto allowancePolicyDto);

  BaseResponse<ResponseCommon> updateAllowancePolicy(DetailAllowancePolicyDto allowancePolicyDto);

  BaseResponse<DetailAllowancePolicyDto> getAllowancePolicy(Long id);

  void deleteAllowancePolicy(Long id);
}
