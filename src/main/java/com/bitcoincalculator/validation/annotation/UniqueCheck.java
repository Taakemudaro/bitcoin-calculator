package com.bitcoincalculator.validation.annotation;

import com.bitcoincalculator.validation.UniqueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;
import java.lang.annotation.*;

@Documented //Javadocドキュメント化
@Constraint(validatedBy = UniqueValidator.class) //カスタムバリデーション指定
@Target({FIELD}) //フィールドにアノテーションを適応
@Retention(RUNTIME) //アノテーションを実行時に保持
public @interface UniqueCheck {
    //falseだった場合、下記メッセージ出力
    String message() default "この年号は既に存在しています！";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
