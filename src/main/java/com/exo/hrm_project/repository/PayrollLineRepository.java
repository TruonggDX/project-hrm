package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.PayrollLineEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollLineRepository extends JpaRepository<PayrollLineEntity, Long> {

  @Query("SELECT p FROM PayrollLineEntity p WHERE p.payrollId=:payrollId")
  List<PayrollLineEntity> findPayrollById(@Param("payrollId") Long payrollId);

  @Query("SELECT p FROM PayrollLineEntity p WHERE p.payrollId IN :ids")
  List<PayrollLineEntity> findByPayrollIds(@Param("ids") List<Long> ids);


}
