package com.bitcoincalculator.controller.form;

import com.bitcoincalculator.validation.annotation.UniqueCheck;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class RecordForm {
    private int id;
    @Min(value = 1000, message = "※ 年号を4桁で入力してください！①")
    @Max(value = 9999, message = "※ 年号を4桁で入力してください！②")
    @NotNull(message = "年号を4桁で入力してください！③")
    @UniqueCheck //カスタムバリデーション
    private int name;
    private Date createdDate;
    private Date updatedDate;
}