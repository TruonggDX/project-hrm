package com.exo.hrm_project.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime createdDate;
  private Long createdBy;
  private LocalDateTime updateDate;
  private Long updateBy;
}
