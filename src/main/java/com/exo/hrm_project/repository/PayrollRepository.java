package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.PayrollEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<PayrollEntity, Long> {

}
