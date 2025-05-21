package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.Cycle;
import com.exo.hrm_project.utils.enums.PayrollType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "payroll")
public class PayrollEntity extends BaseEntity {

  private Long employeeId;
  private Long departmentId;
  private Long positionId;
  @Enumerated(EnumType.STRING)
  private PayrollType type;
  @Enumerated(EnumType.STRING)
  private Cycle cycle;
  private LocalDate startDate;
  private BigDecimal totalAllowanceAmount;
  private String note;
}
