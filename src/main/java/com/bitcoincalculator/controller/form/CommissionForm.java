package com.bitcoincalculator.controller.form;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CommissionForm {
    private int id;
    private int recordId;
    @Min(value = 1, message = "※ 金額を正しく入力してください！")
    @Max(value = 999999999, message = "※ 金額は999999999円以下で入力してください！")
    @NotNull(message = "※ 金額を入力してください！")
    private Integer price;
    private Date createdDate;
    private Date updatedDate;
}