package com.bitcoincalculator.validation;

import com.bitcoincalculator.controller.form.RecordForm;
import com.bitcoincalculator.service.RecordService;
import com.bitcoincalculator.validation.annotation.UniqueCheck;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class UniqueValidator implements ConstraintValidator<UniqueCheck, Integer> {
    @Autowired
    RecordService recordService;

    public void initialize(UniqueCheck uniqueAnnotation) {
    }
    public boolean isValid(Integer name, ConstraintValidatorContext context) {
        RecordForm uniqueRecord = recordService.findRecordByName(name);
        if(uniqueRecord == null) {
            return true;
        }
        return false;
    }
}