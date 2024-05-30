package com.bitcoincalculator.repository;

import com.bitcoincalculator.service.entity.BeginningBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeginningRepository extends JpaRepository<BeginningBalance, Integer> {
    //レコードのname(年号)を指定して取得
    List<BeginningBalance> findByRecordId(Integer recordId);
}
