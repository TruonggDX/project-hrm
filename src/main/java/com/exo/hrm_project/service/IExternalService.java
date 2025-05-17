package com.exo.hrm_project.service;

import com.exo.hrm_project.dto.common.CommonDto;
import java.util.List;

public interface IExternalService {

  CommonDto getUomById(Long id);

  CommonDto getCurrencyById(Long id);

  List<CommonDto> getEmployeeInfoByIds(List<Long> employeeIds);

  List<CommonDto> getDepartmentInfoByIds(List<Long> departmentIds);

  List<CommonDto> getPositionInfoByIds(List<Long> positionIds);
}
