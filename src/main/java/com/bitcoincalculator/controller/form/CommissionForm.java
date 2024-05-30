package com.bitcoincalculator.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CommissionForm {
    private int id;
    private int recordId;
    private int price;
    private Date createdDate;
    private Date updatedDate;
}