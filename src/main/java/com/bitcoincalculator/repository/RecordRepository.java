package com.bitcoincalculator.repository;

import com.bitcoincalculator.service.entity.AnnualRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<AnnualRecord, Integer> {
    List<AnnualRecord> findByName(Integer name);
}