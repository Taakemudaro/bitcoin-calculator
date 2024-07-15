package com.bitcoincalculator.controller.form;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.math.BigDecimal;

@Getter
@Setter
public class SellingForm {
    private int id;
    private int recordId;
    @Positive(message = "※ 数量を正しく入力してください！")
    @NotNull(message = "※ 数量を入力してください！")
    private BigDecimal amount;
    @Min(value = 1, message = "※ 金額を正しく入力してください！")
    @Max(value = 999999999, message = "※ 金額は999999999円以下で入力してください！")
    @NotNull(message = "※ 金額を入力してください！")
    private Integer price;
    private Date createdDate;
    private Date updatedDate;
}