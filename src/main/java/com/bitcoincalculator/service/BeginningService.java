package com.bitcoincalculator.service;

import com.bitcoincalculator.controller.form.BeginningForm;
import com.bitcoincalculator.repository.BeginningRepository;
import com.bitcoincalculator.service.entity.BeginningBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BeginningService {
    @Autowired
    BeginningRepository beginningRepository;

    /*
     *該当年号の売却履歴を全件取得（record画面表示）
     */
    public List<BeginningForm> findBeginningByRecordId(Integer recordId) {
        List<BeginningBalance> results = beginningRepository.findByRecordId(recordId);
        List<BeginningForm> records = setBeginningForm(results);
        return records;
    }
    /*
     *delete実施でのredirect処理時のrecordId取得の為の処理
     */
    public BeginningForm findBeginningById(Integer id) {
        List<BeginningBalance> results = new ArrayList<>();
        results.add((BeginningBalance) beginningRepository.findById(id).orElse(null));
        List<BeginningForm> records = setBeginningForm(results);
        return records.get(0);
    }
    /*
     *取得したEntityからFormに変換する処理
     */
    private List<BeginningForm> setBeginningForm(List<BeginningBalance> results) {
        List<BeginningForm> records = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            BeginningForm record = new BeginningForm();
            BeginningBalance result = results.get(i);
            record.setId(result.getId());
            record.setRecordId(result.getRecordId());
            record.setAmount(result.getAmount());
            record.setPrice(result.getPrice());
            record.setCreatedDate(result.getCreatedDate());
            record.setUpdatedDate(result.getUpdatedDate());
            records.add(record);
        }
        return records;
    }

    /*
     *売却登録処理
     */
    public void saveBeginning(BeginningForm beginningForm) {
        BeginningBalance beginning = setBeginningEntity(beginningForm);
        beginningRepository.save(beginning);
    }

    /*
     *FormからEntityに変換処理
     */
    private BeginningBalance setBeginningEntity(BeginningForm beginningForm) {
        BeginningBalance beginning = new BeginningBalance();
        beginning.setId(beginningForm.getId());
        beginning.setRecordId(beginningForm.getRecordId());
        beginning.setAmount(beginningForm.getAmount());
        beginning.setPrice(beginningForm.getPrice());
        beginning.setCreatedDate(beginningForm.getCreatedDate());
        beginning.setUpdatedDate(beginningForm.getUpdatedDate());
        return beginning;
    }

    public void deleteBeginning(Integer id) {
        beginningRepository.deleteById(id);
    }

}
