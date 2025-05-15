package com.exo.hrm_project.dto.allowance;

import com.exo.hrm_project.dto.common.CommonDto;
import com.exo.hrm_project.utils.enums.DeductionType;
import java.util.List;
import lombok.Data;

@Data
public class AllowanceDto {

  private Long id;
  private String code;
  private String name;
  private List<DeductionType> includeType;
  private String description;
  private String type;
  private Boolean isActive;
  private CommonDto groupAllowance;
  private CommonDto uom;
  private CommonDto currency;
}
