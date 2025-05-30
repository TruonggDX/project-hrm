package com.exo.hrm_project.repository;

import com.exo.hrm_project.entity.RewardEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardRepository extends JpaRepository<RewardEntity, Long>,
    JpaSpecificationExecutor<RewardEntity> {

  List<RewardEntity> findAllByIdIn(List<Long> ids);
}
