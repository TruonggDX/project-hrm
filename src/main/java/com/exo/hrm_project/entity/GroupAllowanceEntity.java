package com.exo.hrm_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "group_allowance")
public class GroupAllowanceEntity extends BaseEntity {

  private String code;
  private String name;
  private String description;
  private Boolean isActive;
  private Long parentId;
}
