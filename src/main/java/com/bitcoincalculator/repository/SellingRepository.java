package com.bitcoincalculator.repository;

import com.bitcoincalculator.service.entity.Selling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SellingRepository extends JpaRepository<Selling, Integer> {
    //レコードのname(年号)を指定して取得
    List<Selling> findByRecordId(Integer recordId);
}
