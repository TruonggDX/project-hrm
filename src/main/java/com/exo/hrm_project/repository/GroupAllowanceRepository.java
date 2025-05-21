package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.GroupAllowanceEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupAllowanceRepository extends JpaRepository<GroupAllowanceEntity, Long>,
    JpaSpecificationExecutor<GroupAllowanceEntity> {

  List<GroupAllowanceEntity> findAllByIdIn(List<Long> ids);

}
