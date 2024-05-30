package com.bitcoincalculator.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="annual_record")
@Getter
@Setter
public class AnnualRecord {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int name;
    @Column
    private Date createdDate;
    @Column
    private Date updatedDate;
}