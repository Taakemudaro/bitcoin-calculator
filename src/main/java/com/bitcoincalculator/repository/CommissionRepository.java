package com.bitcoincalculator.repository;

import com.bitcoincalculator.service.entity.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    //レコードのname(年号)を指定して取得
    List<Commission> findByRecordId(Integer recordId);
}
