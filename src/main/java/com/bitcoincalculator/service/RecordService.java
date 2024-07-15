package com.bitcoincalculator.service;

import com.bitcoincalculator.controller.form.RecordForm;
import com.bitcoincalculator.repository.RecordRepository;
import com.bitcoincalculator.service.entity.AnnualRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordService {
    @Autowired
    RecordRepository recordRepository;

    /*
     *レコード全件取得（top画面表示）
     */
    public List<RecordForm> findAllRecord() {
        List<AnnualRecord> results = recordRepository.findAll();
        List<RecordForm> records = setRecordForm(results);
        return records;
    }
    /*
     *該当idのレコード取得（該当年号のrecord画面表示）
     */
    public RecordForm findRecordById(Integer id) {
        //直接findByIdを取得できない為、ArrayListを作成（findByIdメソッドはOptional型を返す為エラーになる)
        List<AnnualRecord> results = new ArrayList<>();
        //作成したArrayListにOptionalの値をAnnualRecord型で代入（この方法だとエラーにならない）
        //idがなかった場合にnullを返す為、orElseが必要
        results.add((AnnualRecord) recordRepository.findById(id).orElse(null));
//        System.out.println("results:" + results);
        List<RecordForm> records = setRecordForm(results);
        return records.get(0);
    }
//    public List<RecordForm> findRecordById(Integer id) {
//        List<AnnualRecord> results = recordRepository.findById(id);
//        List<RecordForm> records = setRecordForm(results);
//        return records;
//    }

    public RecordForm findRecordByName(Integer name) {
        List<AnnualRecord> results = recordRepository.findByName(name);
        List<RecordForm> records = setRecordForm(results);
        if(records.isEmpty()) {
            return null;
        }
        return records.get(0);
    }
    /*
     *取得したEntityからFormに変換する処理
     */
    private List<RecordForm> setRecordForm(List<AnnualRecord> results) {
        List<RecordForm> records = new ArrayList<>();
        for(int i = 0; i < results.size(); i++) {
            RecordForm record = new RecordForm();
            AnnualRecord result = results.get(i);
            record.setId(result.getId());
            record.setName(result.getName());
            record.setCreatedDate(result.getCreatedDate());
            record.setUpdatedDate(result.getUpdatedDate());
            records.add(record);
        }
        return records;
    }

    /*
     *レコード登録処理
     */
    public void saveRecord(RecordForm recordForm) {
        AnnualRecord record = setRecordEntity(recordForm);
        recordRepository.save(record);
    }
    /*
     *FormからEntityに変換処理
     */
    private AnnualRecord setRecordEntity(RecordForm recordForm) {
        AnnualRecord record = new AnnualRecord();
        record.setId(recordForm.getId());
        record.setName(recordForm.getName());
        record.setCreatedDate(recordForm.getCreatedDate());
        record.setUpdatedDate(recordForm.getUpdatedDate());
        return record;
    }
    /*
     *レコード削除処理
     */
    public void deleteRecord(Integer id) {
        recordRepository.deleteById(id);
    }
}