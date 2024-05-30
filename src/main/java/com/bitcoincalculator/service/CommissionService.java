package com.bitcoincalculator.service;

import com.bitcoincalculator.controller.form.CommissionForm;
import com.bitcoincalculator.service.entity.Commission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bitcoincalculator.repository.CommissionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommissionService {
    @Autowired
    CommissionRepository commissionRepository;

    /*
     *該当年号の手数料履歴を全件取得（record画面表示）
     */
    public List<CommissionForm> findCommissionByRecordId(Integer recordId) {
        List<Commission> results = commissionRepository.findByRecordId(recordId);
        List<CommissionForm> records = setCommissionForm(results);
        return records;
    }
    /*
     *delete実施でのredirect処理時のrecordId取得の為の処理
     */
    public CommissionForm findCommissionById(Integer id) {
        List<Commission> results = new ArrayList<>();
        results.add((Commission) commissionRepository.findById(id).orElse(null));
        List<CommissionForm> records = setCommissionForm(results);
        return records.get(0);
    }
    /*
     *取得したEntityからFormに変換する処理
     */
    private List<CommissionForm> setCommissionForm(List<Commission> results) {
        List<CommissionForm> records = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            CommissionForm record = new CommissionForm();
            Commission result = results.get(i);
            record.setId(result.getId());
            record.setRecordId(result.getRecordId());
            record.setPrice(result.getPrice());
            record.setCreatedDate(result.getCreatedDate());
            record.setUpdatedDate(result.getUpdatedDate());
            records.add(record);
        }
        return records;
    }

    /*
     *手数料登録処理
     */
    public void saveCommission(CommissionForm commissionForm) {
        Commission commission = setCommissionEntity(commissionForm);
        commissionRepository.save(commission);
    }

    /*
     *FormからEntityに変換処理
     */
    private Commission setCommissionEntity(CommissionForm commissionForm) {
        Commission commission = new Commission();
        commission.setId(commissionForm.getId());
        commission.setRecordId(commissionForm.getRecordId());
        commission.setPrice(commissionForm.getPrice());
        commission.setCreatedDate(commissionForm.getCreatedDate());
        commission.setUpdatedDate(commissionForm.getUpdatedDate());
        return commission;
    }

    public void deleteCommission(Integer id) {
        commissionRepository.deleteById(id);
    }
}
