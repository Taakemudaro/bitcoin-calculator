package com.bitcoincalculator.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
public class SellingForm {
    private int id;
    private int recordId;
    private BigDecimal amount;
    private int price;
    private Date createdDate;
    private Date updatedDate;
}