package com.bitcoincalculator.controller.form;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class RecordForm {
    private int id;
    private int name;
    private Date createdDate;
    private Date updatedDate;
}