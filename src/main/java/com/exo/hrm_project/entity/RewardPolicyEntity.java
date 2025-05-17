package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.ApplicableObject;
import com.exo.hrm_project.utils.enums.PolicyType;
import com.exo.hrm_project.utils.enums.State;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;

@Entity
@Data
@Table(name = "reward_policy")
public class RewardPolicyEntity extends BaseEntity {

  private String code;
  private String name;
  private String description;
  @Enumerated(EnumType.STRING)
  private PolicyType type;
  private LocalDate startDate;
  private LocalDate endDate;
  @Enumerated(EnumType.STRING)
  private State state;
  @Enumerated(EnumType.STRING)
  private ApplicableObject applicableType;
}
