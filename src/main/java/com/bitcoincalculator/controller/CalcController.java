package com.bitcoincalculator.controller;

import com.bitcoincalculator.controller.form.*;
import com.bitcoincalculator.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public ModelAndView top(Model model) {
        ModelAndView mav = new ModelAndView();
        if(!model.containsAttribute("formAddRecord")) {
            //レコード登録処理用のform作成
            RecordForm formAddRecord = new RecordForm();
            //記入欄のデフォルト年号取得
            LocalDate date = LocalDate.now();
            int year = date.getYear();
            formAddRecord.setName(year);
            mav.addObject("formAddRecord", formAddRecord);
        }
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
    public ModelAndView addRecord(@Valid @ModelAttribute("formAddRecord") RecordForm recordForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formAddRecord", recordForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formAddRecord", bindingResult);
            return new ModelAndView("redirect:/");
        }
        recordService.saveRecord(recordForm);
        return new ModelAndView("redirect:/");
    }
    /*
     *レコード削除処理
     */
    @DeleteMapping("/delete/record/{id}")
    public ModelAndView deleteRecord(@PathVariable Integer id) {
        recordService.deleteRecord(id);
        return new ModelAndView("redirect:/");
    }
    /*
     *レコード名編集処理
     */
    @GetMapping("/editRecord/{id}")
    public ModelAndView editRecordForm(@PathVariable Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        RecordForm CurrentRecordForm = recordService.findRecordById(id);
        if(!model.containsAttribute("formEditRecord")) {
            RecordForm recordForm = recordService.findRecordById(id);
            mav.addObject("formEditRecord", recordForm);
        }
        mav.addObject("formCurrentRecord", CurrentRecordForm);
        mav.setViewName("/editRecord");
        return mav;
    }

    /*
     *レコード名編集処理
     */
    @PutMapping("/put/record")
    public ModelAndView edit(@Valid @ModelAttribute("formEditRecord") RecordForm recordForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formEditRecord", recordForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formEditRecord", bindingResult);
            return new ModelAndView("redirect:/editRecord/" + recordForm.getId());
        }
        recordService.saveRecord(recordForm);
        return new ModelAndView("redirect:/");
    }
    /*
     * レコードページ表示処理(各年号ごと)
     * → 年始残高・購入・売却履歴の全件取得、税金の計算結果を表示
     */
    @GetMapping("/record/{id}")
    //バリデーション情報ある場合はModelに情報あり
    public ModelAndView registerRecord(@PathVariable Integer id, Model model) {
        ModelAndView mav = new ModelAndView();

        //レコード年号の表示処理
        RecordForm record = recordService.findRecordById(id);
        mav.addObject("annualName", record);

        //手数料の追加フォーム作成
        if(!model.containsAttribute("formAddCommission")) {
            CommissionForm commissionForm = new CommissionForm();
            commissionForm.setRecordId(id);
            mav.addObject("formAddCommission", commissionForm);
        }

        //年始残高の追加フォーム作成
        if (!model.containsAttribute("formAddBeginning")) {
            BeginningForm beginningForm = new BeginningForm();
            beginningForm.setRecordId(id);
            mav.addObject("formAddBeginning", beginningForm);
        }
        //年始残高履歴の取得処理
        List<BeginningForm> beginningForms = beginningService.findBeginningByRecordId(id);
        mav.addObject("formSelectBeginnings",beginningForms);

        //購入フォーム用オブジェクト作成
        if (!model.containsAttribute("formAddPurchase")) {
            PurchaseForm purchaseForm = new PurchaseForm();
            purchaseForm.setRecordId(id);
            mav.addObject("formAddPurchase", purchaseForm);
        }

        //売却フォーム用オブジェクト作成
        if (!model.containsAttribute("formAddSelling")) {
            SellingForm sellingForm = new SellingForm();
            sellingForm.setRecordId(id);
            mav.addObject("formAddSelling", sellingForm);
        }

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
        BeginningForm calculateBeginning = calculateService.calculateTotalBeginning(beginningForms);

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
    public ModelAndView addCommission(@Valid @ModelAttribute("formAddCommission") CommissionForm commissionForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formAddCommission", commissionForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formAddCommission", bindingResult);
            return new ModelAndView("redirect:/record/" + commissionForm.getRecordId());
        }
        commissionService.saveCommission(commissionForm);
        return new ModelAndView("redirect:/record/" + commissionForm.getRecordId());
    }


    /*
     *年始残高登録処理
     */
    @PostMapping("/beginning")
    public ModelAndView addBeginning(@Valid @ModelAttribute("formAddBeginning") BeginningForm beginningForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formAddBeginning", beginningForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formAddBeginning", bindingResult);
            return new ModelAndView("redirect:/record/" + beginningForm.getRecordId());
        }
        beginningService.saveBeginning(beginningForm);
        return new ModelAndView("redirect:/record/" + beginningForm.getRecordId());
    }

    /*
     *購入登録処理
     */
    @PostMapping("/purchase")
    public ModelAndView addPurchase(@Valid @ModelAttribute("formAddPurchase") PurchaseForm purchaseForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        //validationCheck
        if (bindingResult.hasErrors()) {
            //redirect後の処理で引継ぎ
            redirectAttributes.addFlashAttribute("formAddPurchase", purchaseForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formAddPurchase", bindingResult);
            return new ModelAndView("redirect:/record/"+ purchaseForm.getRecordId());
        }
        purchaseService.savePurchase(purchaseForm);
        return new ModelAndView("redirect:/record/" + purchaseForm.getRecordId());
    }
    /*
     *売却登録処理
     */
    @PostMapping("/selling")
    public ModelAndView addSelling(@Valid @ModelAttribute("formAddSelling") SellingForm sellingForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formAddSelling", sellingForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formAddSelling", bindingResult);
            return new ModelAndView("redirect:/record/" + sellingForm.getRecordId());
        }
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
    public ModelAndView editCommissionForm(@PathVariable Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!model.containsAttribute("formEditCommission")) {
            CommissionForm commissionForm = commissionService.findCommissionById(id);
            mav.addObject("formEditCommission", commissionForm);
        }
        mav.setViewName("/editCommission");
        return mav;
    }
    /*
     *年始残高登録の編集画面表示処理
     */
    @GetMapping("/editBeginning/{id}")
    public ModelAndView editBeginningForm(@PathVariable Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!model.containsAttribute("formEditBeginning")) {
            BeginningForm beginningForm = beginningService.findBeginningById(id);
            mav.addObject("formEditBeginning", beginningForm);
        }
        mav.setViewName("/editBeginning");
        return mav;
    }
    /*
     * 購入登録の編集画面表示処理
     */
    @GetMapping("/editPurchase/{id}")
    public ModelAndView editPurchaseForm(@PathVariable Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        if(!model.containsAttribute("formEditPurchase")) {
            PurchaseForm purchaseForm = purchaseService.findPurchaseById(id);
            mav.addObject("formEditPurchase", purchaseForm);
        }
        mav.setViewName("/editPurchase");
        return mav;
    }
    /*
     * 売却登録の編集画面表示処理
     */
    @GetMapping("/editSelling/{id}")
    public ModelAndView editSellingForm(@PathVariable Integer id, Model model) {
        ModelAndView mav = new ModelAndView();
        if (!model.containsAttribute("formEditSelling")) {
            SellingForm sellingForm = sellingService.findSellingById(id);
            mav.addObject("formEditSelling", sellingForm);
        }
        mav.setViewName("/editSelling");
        return mav;
    }
    /*
     *手数料登録の編集処理
     */
    @PutMapping("/put/commission")
    public ModelAndView editCommission(@Valid @ModelAttribute("formEditCommission") CommissionForm commissionForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formEditCommission", commissionForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formEditCommission", bindingResult);
            return new ModelAndView("redirect:/editCommission/" + commissionForm.getId());
        }
        commissionService.saveCommission(commissionForm);
        return new ModelAndView("redirect:/record/" + commissionForm.getRecordId());
    }
    /*
     *年始残高登録の編集処理
     */
    @PutMapping("/put/beginning")
    public ModelAndView editBeginning(@Valid @ModelAttribute("formEditBeginning") BeginningForm beginningForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formEditBeginning", beginningForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formEditBeginning", bindingResult);
            return new ModelAndView("redirect:/editBeginning/" + beginningForm.getId());
        }
        beginningService.saveBeginning(beginningForm);
        return new ModelAndView("redirect:/record/" + beginningForm.getRecordId());
    }
    /*
     *購入登録の編集処理
     */
    @PutMapping("/put/purchase")
    public ModelAndView editPurchase(@Valid @ModelAttribute("formEditPurchase") PurchaseForm purchaseForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formEditPurchase", purchaseForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formEditPurchase", bindingResult);
            return new ModelAndView("redirect:/editPurchase/" + purchaseForm.getId());
        }
//        purchaseService.editPurchase(purchaseForm, purchaseForm.getId());
        purchaseService.savePurchase(purchaseForm);
        return new ModelAndView("redirect:/record/" + purchaseForm.getRecordId());
    }
    /*
     * 売却登録の編集処理
     */
    @PutMapping("/put/selling")
    public ModelAndView editSelling(@Valid @ModelAttribute("formEditSelling") SellingForm sellingForm, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("formEditSelling", sellingForm);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.formEditSelling", bindingResult);
            return new ModelAndView("redirect:/editSelling/" + sellingForm.getId());
        }
        sellingService.saveSelling(sellingForm);
        return new ModelAndView("redirect:/record/" + sellingForm.getRecordId());
    }

}