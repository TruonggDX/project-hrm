package com.exo.hrm_project.entity;

import com.exo.hrm_project.utils.enums.AmountItem;
import com.exo.hrm_project.utils.enums.PayrollLineType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "payroll_line")
public class PayrollLineEntity extends BaseEntity {

  @Enumerated(EnumType.STRING)
  private PayrollLineType type;
  private Long groupTargetId;
  private Long groupId;
  private BigDecimal amount;
  @Enumerated(EnumType.STRING)
  private AmountItem amountItem;
  private BigDecimal taxableAmount;
  private BigDecimal insuranceAmount;
  private Long payrollId;

}
