package com.bitcoincalculator.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.math.BigDecimal;
import jakarta.validation.constraints.*;

@Getter
@Setter
public class PurchaseForm {
    private int id;
    private int recordId;
    @Positive(message = "※ 数量を正しく入力してください！①")
    @NotNull(message = "※ 数量を入力してください！②")
    private BigDecimal amount;
    @Min(value = 1, message = "※ 金額を正しく入力してください！③")
    @Max(value = 999999999, message = "※ 金額は999999999円以下で入力してください！④")
    @NotNull(message = "※ 金額を入力してください！⑤")
    private Integer price;
    private Date createdDate;
    private Date updatedDate;
}