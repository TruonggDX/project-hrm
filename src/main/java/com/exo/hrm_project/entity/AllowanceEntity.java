package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.AllowanceRewardType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "allowance")
public class AllowanceEntity extends BaseEntity {

  private String code;
  private String name;
  @Enumerated(EnumType.STRING)
  private AllowanceRewardType type;
  private String description;
  private String includeType;
  private Boolean isActive;
  private Long uomId;
  private Long currencyId;
  private Long groupAllowanceId;
}
