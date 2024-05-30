package com.bitcoincalculator.service;

import com.bitcoincalculator.controller.form.SellingForm;
import com.bitcoincalculator.repository.SellingRepository;
import com.bitcoincalculator.service.entity.Selling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SellingService {
    @Autowired
    SellingRepository sellingRepository;

    /*
     *該当年号の売却履歴を全件取得（record画面表示）
     */
    public List<SellingForm> findSellingByRecordId(Integer recordId) {
        List<Selling> results = sellingRepository.findByRecordId(recordId);
        List<SellingForm> records = setSellingForm(results);
        return records;
    }
    /*
     *delete実施でのredirect処理時のrecordId取得の為の処理
     */
    public SellingForm findSellingById(Integer id) {
        List<Selling> results = new ArrayList<>();
        results.add((Selling) sellingRepository.findById(id).orElse(null));
        List<SellingForm> records = setSellingForm(results);
        return records.get(0);
    }
    /*
     *取得したEntityからFormに変換する処理
     */
    private List<SellingForm> setSellingForm(List<Selling> results) {
        List<SellingForm> records = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            SellingForm record = new SellingForm();
            Selling result = results.get(i);
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
    public void saveSelling(SellingForm sellingForm) {
        Selling selling = setSellingEntity(sellingForm);
        sellingRepository.save(selling);
    }

    /*
     *FormからEntityに変換処理
     */
    private Selling setSellingEntity(SellingForm sellingForm) {
        Selling selling = new Selling();
        selling.setId(sellingForm.getId());
        selling.setRecordId(sellingForm.getRecordId());
        selling.setAmount(sellingForm.getAmount());
        selling.setPrice(sellingForm.getPrice());
        selling.setCreatedDate(sellingForm.getCreatedDate());
        selling.setUpdatedDate(sellingForm.getUpdatedDate());
        return selling;
    }

    public void deleteSelling(Integer id) {
        sellingRepository.deleteById(id);
    }
}