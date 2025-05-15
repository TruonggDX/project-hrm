package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.Cycle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "allowance_policy_line")
public class AllowancePolicyLineEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private Cycle cycle;
  private BigDecimal amount;
  @Column(name = "allowance_policy_id")
  private Long allowancePolicyId;
  private Long allowanceId;
}
