package com.exo.hrm_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "group_reward")
public class GroupRewardEntity extends BaseEntity {

  private String code;
  private String name;
  private String description;
  private Long parentId;
}
