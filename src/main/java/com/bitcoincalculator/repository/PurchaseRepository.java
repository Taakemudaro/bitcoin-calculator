package com.bitcoincalculator.repository;

import com.bitcoincalculator.service.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer> {
    //レコードのname(年号)を指定して取得
    List<Purchase> findByRecordId(Integer recordId);
}
