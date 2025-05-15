package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.AllowanceRewardType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "reward")
public class RewardEntity extends BaseEntity {

  private String code;
  private String name;
  private String includeType;
  private Long groupRewardId;
  @Enumerated(EnumType.STRING)
  private AllowanceRewardType type;
  private Long uomId;
  private Long currencyId;
  private String description;
}
