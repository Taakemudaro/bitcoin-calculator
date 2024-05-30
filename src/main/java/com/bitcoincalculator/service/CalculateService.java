package com.bitcoincalculator.service;

import com.bitcoincalculator.controller.form.*;
import com.bitcoincalculator.service.entity.Purchase;
import com.bitcoincalculator.repository.PurchaseRepository;
import com.bitcoincalculator.repository.SellingRepository;
import com.bitcoincalculator.service.entity.Selling;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
//登録した購入履歴、売却履歴、年始残高情報を取得して計算 → 合計額を出す処理
public class CalculateService {
    @Autowired
    PurchaseRepository purchaseRepository;
    @Autowired
    SellingRepository sellingRepository;

    /*
     *購入履歴情報を取得してAmountとPriceの合計額を抽出する処理
     */
    public PurchaseForm calculateTotalPurchase(List<PurchaseForm> records) {
        //変換したFormのAmountとPriceを合算
        PurchaseForm totalRecord = calculatePurchase(records);
        return totalRecord;
    }

    /*
     *Form情報からAmountとPriceを合算
     */
    private PurchaseForm calculatePurchase(List<PurchaseForm> records) {
        //合算用にForm作成
        PurchaseForm totalRecord = new PurchaseForm();
        //合算用の変数定義
        BigDecimal totalAmount = new BigDecimal(0);
        int totalPrice = 0;

        //合算処理
        for(int i = 0; i < records.size(); i++) {
            PurchaseForm record = records.get(i);
            totalAmount = totalAmount.add(record.getAmount());
            totalPrice += record.getPrice();
        }
        totalRecord.setAmount(totalAmount);
        totalRecord.setPrice(totalPrice);
        return totalRecord;
    }

    /*
     *売却履歴情報を取得してAmountとPriceの合計額を抽出する処理
     */
    public SellingForm calculateTotalSelling(List<SellingForm> records) {
        //FormのAmountとPriceを合算
        SellingForm totalRecord = calculateSelling(records);
        return totalRecord;
    }

    /*
     *Form情報からAmountとPriceを合算
     */
    private SellingForm calculateSelling(List<SellingForm> records) {
        //合算用にForm作成
        SellingForm totalRecord = new SellingForm();
        //合算用の変数定義
        BigDecimal totalAmount = new BigDecimal(0);
        int totalPrice = 0;

        //合算処理
        for(int i = 0; i < records.size(); i++) {
            SellingForm record = records.get(i);
            totalAmount = totalAmount.add(record.getAmount());
            totalPrice += record.getPrice();
        }
        totalRecord.setAmount(totalAmount);
        totalRecord.setPrice(totalPrice);
        return totalRecord;
    }

    /*
     *手数料履歴情報を取得してAmountとPriceの合計額を抽出する処理
     */
    public CommissionForm calculateTotalCommission(List<CommissionForm> records) {
        //変換したFormのAmountとPriceを合算
        CommissionForm totalRecord = calculateCommission(records);
        return totalRecord;
    }

    /*
     *Form情報からAmountとPriceを合算
     */
    private CommissionForm calculateCommission(List<CommissionForm> records) {
        //合算用にForm作成
        CommissionForm totalRecord = new CommissionForm();
        //合算用の変数定義
        int totalPrice = 0;

        //合算処理
        for(int i = 0; i < records.size(); i++) {
            CommissionForm record = records.get(i);
            totalPrice += record.getPrice();
        }
        totalRecord.setPrice(totalPrice);
        return totalRecord;
    }

    /*
     *年始残高履歴情報を取得してAmountとPriceの合計額を抽出する処理
     *(年始残高は1つのみしか登録できないが、List化してしまっている為)
     */
    public BeginningForm calculateTotalBeginning(List<BeginningForm> records) {
        //変換したFormのAmountとPriceを合算
        BeginningForm totalRecord = calculateBeginning(records);
        return totalRecord;
    }
    /*
     *Form情報からAmountとPriceを合算
     */
    private BeginningForm calculateBeginning(List<BeginningForm> records) {
        //合算用にForm作成
        BeginningForm totalRecord = new BeginningForm();
        //合算用の変数定義
        BigDecimal totalAmount = new BigDecimal(0);
        int totalPrice = 0;

        //合算処理
        for(int i = 0; i < records.size(); i++) {
            BeginningForm record = records.get(i);
            totalAmount = totalAmount.add(record.getAmount());
            totalPrice += record.getPrice();
        }
        totalRecord.setAmount(totalAmount);
        totalRecord.setPrice(totalPrice);
        return totalRecord;
    }
    /*
     *総平均単価と原価売却、今年の年始残高、経費、所得金額の算出処理
     */
    public CalculateForm calculatePrice(BeginningForm calculateBeginning, PurchaseForm calculatePurchase, SellingForm calculateSelling, CommissionForm calculateCommission) {
        CalculateForm calculateForm = new CalculateForm();
        //初期化
        BigDecimal averageBigDecimal = BigDecimal.ZERO;
        BigDecimal sellingCostBigDecimal = BigDecimal.ZERO;
        BigDecimal beginningAmount = BigDecimal.ZERO;
        BigDecimal beginningPriceBigDecimal = BigDecimal.ZERO;

        //Priceはint型の為、BigDecimalの値と計算できるようにする為、int→BigDecimalに変換
        BigDecimal beginningBigDecimal = BigDecimal.valueOf(calculateBeginning.getPrice());
        BigDecimal purchaseBigDecimal = BigDecimal.valueOf(calculatePurchase.getPrice());

        //計算処理
        //※下記で小数点を使用した割り算を使用する場合、RoundingModeクラスで丸め処理をする必要あり
        averageBigDecimal = (beginningBigDecimal.add(purchaseBigDecimal)).divide(calculateBeginning.getAmount().add(calculatePurchase.getAmount()), RoundingMode.HALF_UP);
        sellingCostBigDecimal = averageBigDecimal.multiply(calculateSelling.getAmount());
        beginningAmount = (calculateBeginning.getAmount().add(calculatePurchase.getAmount()).subtract(calculateSelling.getAmount()));
        beginningPriceBigDecimal = averageBigDecimal.multiply(beginningAmount);

        //Priceの値をint型に戻し
        int averagePrice = averageBigDecimal.intValue();
        int sellingCost = sellingCostBigDecimal.intValue();
        int beginningPrice = beginningPriceBigDecimal.intValue();
        int totalCost = sellingCost + calculateCommission.getPrice();
        int incomeAmount = calculateSelling.getPrice() - totalCost;

        calculateForm.setAveragePrice(averagePrice);
        calculateForm.setSellingCost(sellingCost);
        //年始残高の数量の値は2桁で丸めるように設定
        calculateForm.setBeginningAmount(beginningAmount);
        calculateForm.setBeginningPrice(beginningPrice);
        calculateForm.setTotalCost(totalCost);
        calculateForm.setIncomeAmount(incomeAmount);
        return calculateForm;
    }
}
