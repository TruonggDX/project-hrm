package com.exo.hrm_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "allowance_policy_applicable_target")
public class AllowancePolicyApplicableTargetEntity extends BaseEntity {

  private Long allowancePolicyId;
  private Long targetId;

}
