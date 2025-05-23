package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.AllowanceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AllowanceRepository extends JpaRepository<AllowanceEntity, Long>,
    JpaSpecificationExecutor<AllowanceEntity> {

  List<AllowanceEntity> findAllByIdIn(List<Long> ids);
}
