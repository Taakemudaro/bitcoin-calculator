package com.bitcoincalculator.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CalculateForm {
    private int averagePrice;
    private int sellingCost;
    private BigDecimal beginningAmount;
    private int beginningPrice;
    private int totalCost;
    private int incomeAmount;
}