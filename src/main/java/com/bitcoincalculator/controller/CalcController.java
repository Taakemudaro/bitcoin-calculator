package com.bitcoincalculator.controller;

import com.bitcoincalculator.controller.form.*;
import com.bitcoincalculator.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
public class CalcController {
    @Autowired
    RecordService recordService;
    @Autowired
    BeginningService beginningService;
    @Autowired
    PurchaseService purchaseService;
    @Autowired
    SellingService sellingService;
    @Autowired
    CalculateService calculateService;
    @Autowired
    CommissionService commissionService;

    /*
     *top画面表示処理（レコード全件取得）
     */
    @GetMapping
    public ModelAndView top() {
        ModelAndView mav = new ModelAndView();
        //レコード登録処理用のform作成
        RecordForm formAddRecord = new RecordForm();
        //記入欄のデフォルト年号取得
        LocalDate date = LocalDate.now();
        int year = date.getYear();
        formAddRecord.setName(year);
        mav.addObject("formAddRecord", formAddRecord);
        //レコード履歴取得処理
        List<RecordForm> recordForm = recordService.findAllRecord();
        mav.setViewName("/top");
        mav.addObject("records", recordForm);
        return mav;
    }

    /*
     *レコード登録処理
     */
    @PostMapping("/add")
    public ModelAndView addRecord(@ModelAttribute("formRecord") RecordForm recordForm) {
        recordService.saveRecord(recordForm);
        return new ModelAndView("redirect:/");
    }
    /*
     * レコードページ表示処理(各年号ごと)
     * → 年始残高・購入・売却履歴の全件取得、税金の計算結果を表示
     */
    @GetMapping("/record/{id}")
    public ModelAndView registerRecord(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        //レコード年号の表示処理
        RecordForm record = recordService.findRecordById(id);
        mav.addObject("annualName", record);

        //手数料の追加フォーム作成
        CommissionForm commissionForm = new CommissionForm();
        commissionForm.setRecordId(id);
        mav.addObject("formAddCommission", commissionForm);

        //年始残高の追加フォーム作成
        BeginningForm beginningAmount = new BeginningForm();
        beginningAmount.setRecordId(id);
        mav.addObject("formAddBeginning", beginningAmount);
        //年始残高履歴の取得処理
        List<BeginningForm> beginningForm = beginningService.findBeginningByRecordId(id);
        mav.addObject("formSelectBeginnings",beginningForm);
        //購入フォーム用オブジェクト作成
        PurchaseForm purchaseAmount = new PurchaseForm();
        purchaseAmount.setRecordId(id);
        mav.addObject("formAddPurchase", purchaseAmount);
        //売却フォーム用オブジェクト作成
        SellingForm sellingAmount = new SellingForm();
        sellingAmount.setRecordId(id);
        mav.addObject("formAddSelling", sellingAmount);

        //手数料の取得履歴処理
        List<CommissionForm> commissionForms = commissionService.findCommissionByRecordId(id);
        mav.addObject("commissionAmounts", commissionForms);

        //購入履歴の取得処理（対象年号のみの購入履歴情報の取得）
        List<PurchaseForm> purchaseForms = purchaseService.findPurchaseByRecordId(id);
        mav.addObject("purchaseAmounts", purchaseForms);
        //売却履歴の取得処理（対象年号のみの売却履歴情報の取得）
        List<SellingForm> sellingForms = sellingService.findSellingByRecordId(id);
        mav.addObject("sellingAmounts", sellingForms);

        //金額集計処理の結果情報
        PurchaseForm calculatePurchase = calculateService.calculateTotalPurchase(purchaseForms);
        mav.addObject("totalPurchaseAmount", calculatePurchase);
        SellingForm calculateSelling =  calculateService.calculateTotalSelling(sellingForms);
        mav.addObject("totalSellingAmount", calculateSelling);
        CommissionForm calculateCommission = calculateService.calculateTotalCommission(commissionForms);
        mav.addObject("totalCommissionAmount", calculateCommission);
        BeginningForm calculateBeginning = calculateService.calculateTotalBeginning(beginningForm);

        //総平均単価と売却原価、今年の年始残高、経費、所得金額の算出結果を取得
        CalculateForm calculateForm = calculateService.calculatePrice(calculateBeginning, calculatePurchase, calculateSelling, calculateCommission);
        mav.addObject("calculatePrice", calculateForm);
        mav.setViewName("/record");

        return mav;
    }

    /*
     *手数料登録処理
     */
    @PostMapping("/commission")
    public ModelAndView addCommission(@ModelAttribute("formAddCommission") CommissionForm commissionForm) {
        commissionService.saveCommission(commissionForm);
        return new ModelAndView("redirect:/record/" + commissionForm.getRecordId());
    }


    /*
     *年始残高登録処理
     */
    @PostMapping("/beginning")
    public ModelAndView addBeginning(@ModelAttribute("formAddBeginning") BeginningForm beginningForm) {
        beginningService.saveBeginning(beginningForm);
        return new ModelAndView("redirect:/record/" + beginningForm.getRecordId());
    }

    /*
     *購入登録処理
     */
    @PostMapping("/purchase")
    public ModelAndView addPurchase(@ModelAttribute("formAddPurchase") PurchaseForm purchaseForm) {
        purchaseService.savePurchase(purchaseForm);
        return new ModelAndView("redirect:/record/" + purchaseForm.getRecordId());
    }
    /*
     *売却登録処理
     */
    @PostMapping("/selling")
    public ModelAndView addSelling(@ModelAttribute("formAddSelling") SellingForm sellingForm) {
        sellingService.saveSelling(sellingForm);
        return new ModelAndView("redirect:/record/" + sellingForm.getRecordId());
    }

    /*
     *手数料削除処理
     */
    @DeleteMapping("/delete/commission/{id}")
    public ModelAndView deleteCommission(@PathVariable Integer id) {
        CommissionForm commissionForm = commissionService.findCommissionById(id);
        commissionService.deleteCommission(id);
        return new ModelAndView("redirect:/record/" + commissionForm.getRecordId());
    }

    /*
     *年始残高削除処理
     */
    @DeleteMapping("/delete/beginning/{id}")
    public ModelAndView deleteBeginning(@PathVariable Integer id) {
        BeginningForm beginningForm = beginningService.findBeginningById(id);
        beginningService.deleteBeginning(id);
        return new ModelAndView("redirect:/record/" + beginningForm.getRecordId());
    }

    /*
     *購入登録削除処理
     */
    @DeleteMapping("/delete/purchase/{id}")
    public ModelAndView deletePurchase(@PathVariable Integer id) {
        PurchaseForm purchaseForm = purchaseService.findPurchaseById(id);
        purchaseService.deletePurchase(id);
        return new ModelAndView("redirect:/record/" + purchaseForm.getRecordId());
    }

    /*
     *売却登録削除処理
     */
    @DeleteMapping("/delete/selling/{id}")
    public ModelAndView deleteSelling(@PathVariable Integer id) {
        SellingForm sellingForm = sellingService.findSellingById(id);
        sellingService.deleteSelling(id);
        return new ModelAndView("redirect:/record/" + sellingForm.getRecordId());
    }
    /*
     *手数料登録の編集画面表示処理
     */
    @GetMapping("/editCommission/{id}")
    public ModelAndView editCommissionForm(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        CommissionForm commissionForm = commissionService.findCommissionById(id);
        mav.addObject("formEditCommission", commissionForm);
        mav.setViewName("/editCommission");
        return mav;
    }
    /*
     *年始残高登録の編集画面表示処理
     */
    @GetMapping("/editBeginning/{id}")
    public ModelAndView editBeginningForm(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        BeginningForm beginningForm = beginningService.findBeginningById(id);
        mav.addObject("formEditBeginning", beginningForm);
        mav.setViewName("/editBeginning");
        return mav;
    }
    /*
     * 購入登録の編集画面表示処理
     */
    @GetMapping("/editPurchase/{id}")
    public ModelAndView editPurchaseForm(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        PurchaseForm purchaseForm = purchaseService.findPurchaseById(id);
        mav.addObject("formEditPurchase", purchaseForm);
        mav.setViewName("/editPurchase");
        return mav;
    }
    /*
     * 売却登録の編集画面表示処理
     */
    @GetMapping("/editSelling/{id}")
    public ModelAndView editSellingForm(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        SellingForm sellingForm = sellingService.findSellingById(id);
        mav.addObject("formEditSelling", sellingForm);
        mav.setViewName("/editSelling");
        return mav;
    }
    /*
     *手数料登録の編集処理
     */
    @PutMapping("/put/commission")
    public ModelAndView editCommission(@ModelAttribute("formEditCommission") CommissionForm commissionForm) {
        commissionService.saveCommission(commissionForm);
        return new ModelAndView("redirect:/record/" + commissionForm.getRecordId());
    }
    @PutMapping("/put/beginning")
    public ModelAndView editBeginning(@ModelAttribute("formEditBeginning") BeginningForm beginningForm) {
        beginningService.saveBeginning(beginningForm);
        return new ModelAndView("redirect:/record/" + beginningForm.getRecordId());
    }
    /*
     *購入登録の編集処理
     */
    @PutMapping("/put/purchase")
    public ModelAndView editPurchase(@ModelAttribute("formEditPurchase") PurchaseForm purchaseForm) {
//        purchaseService.editPurchase(purchaseForm, purchaseForm.getId());
        purchaseService.savePurchase(purchaseForm);
        return new ModelAndView("redirect:/record/" + purchaseForm.getRecordId());
    }
    /*
     * 売却登録の編集処理
     */
    @PutMapping("/put/selling")
    public ModelAndView editSelling(@ModelAttribute("formEditSelling") SellingForm sellingForm) {
        sellingService.saveSelling(sellingForm);
        return new ModelAndView("redirect:/record/" + sellingForm.getRecordId());
    }

}