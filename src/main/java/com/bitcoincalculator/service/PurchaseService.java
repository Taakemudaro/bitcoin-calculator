package com.bitcoincalculator.service;

import com.bitcoincalculator.controller.form.PurchaseForm;
import com.bitcoincalculator.repository.PurchaseRepository;
import com.bitcoincalculator.service.entity.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class PurchaseService {
    @Autowired
    PurchaseRepository purchaseRepository;

    /*
     *該当年号の購入履歴を全件取得（record画面表示）
     */
    public List<PurchaseForm> findPurchaseByRecordId(Integer recordId) {
        List<Purchase> results = purchaseRepository.findByRecordId(recordId);
        List<PurchaseForm> records = setPurchaseForm(results);
        return records;
    }
    /*
     *delete実施でのredirect処理時のrecordId取得の為の処理
     */
    public PurchaseForm findPurchaseById(Integer id) {
        List<Purchase> results = new ArrayList<>();
        results.add((Purchase) purchaseRepository.findById(id).orElse(null));
        List<PurchaseForm> records = setPurchaseForm(results);
        return records.get(0);
    }
    /*
     *取得したEntityからFormに変換する処理
     */
    private List<PurchaseForm> setPurchaseForm(List<Purchase> results) {
        List<PurchaseForm> records = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            PurchaseForm record = new PurchaseForm();
            Purchase result = results.get(i);
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
     *購入登録処理
     */
    public void savePurchase(PurchaseForm purchaseForm) {
        Purchase purchase = setPurchaseEntity(purchaseForm);
        purchaseRepository.save(purchase);
    }

    /*
     *FormからEntityに変換処理
     */
    private Purchase setPurchaseEntity(PurchaseForm purchaseForm) {
        Purchase purchase = new Purchase();
        purchase.setId(purchaseForm.getId());
        purchase.setRecordId(purchaseForm.getRecordId());
        purchase.setAmount(purchaseForm.getAmount());
        purchase.setPrice(purchaseForm.getPrice());
        purchase.setCreatedDate(purchaseForm.getCreatedDate());
        purchase.setUpdatedDate(purchaseForm.getUpdatedDate());
        return purchase;
    }






//    /*
//     *購入編集処理（JpaのAuditingEntityListener機能を使用している為、下記不要）
//     */
//    public void editPurchase(PurchaseForm purchaseForm, Integer id) {
//        List<Purchase> results = new ArrayList<>();
//        results.add((Purchase) purchaseRepository.findById(id).orElse(null));
//        Purchase purchaseCreatedDate = results.get(0);
//
//        Purchase purchase = setEditPurchaseEntity(purchaseForm);
//        purchase.setCreatedDate(purchaseCreatedDate.getCreatedDate());
//        purchaseRepository.save(purchase);
//    }
//    /*
//     *FormからEntityに変換処理
//     */
//    private Purchase setEditPurchaseEntity(PurchaseForm purchaseForm) {
//        Purchase purchase = new Purchase();
//        purchase.setId(purchaseForm.getId());
//        purchase.setRecordId(purchaseForm.getRecordId());
//        purchase.setAmount(purchaseForm.getAmount());
//        purchase.setPrice(purchaseForm.getPrice());
//        purchase.setUpdatedDate(purchaseForm.getUpdatedDate());
//        return purchase;
//    }

    /*
     * 購入登録の削除処理
     */
    public void deletePurchase(Integer id) {
        purchaseRepository.deleteById(id);
    }
}