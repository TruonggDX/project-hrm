package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.common.CommonDto;

public interface IExternalService {

  CommonDto getUomById(Long id);

  CommonDto getCurrencyById(Long id);

  CommonDto getDepartmentById(Long id);

  CommonDto getEmployeeById(Long id);

  CommonDto getPositionById(Long id);
}
