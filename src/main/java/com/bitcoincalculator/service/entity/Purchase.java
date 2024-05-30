package com.bitcoincalculator.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.math.BigDecimal;

@Entity
//Entityの監査機能適応（作成日時と更新日時の設定）
@EntityListeners(AuditingEntityListener.class)
@Table(name="purchase_amount")
@Getter
@Setter
public class Purchase {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private int recordId;
    @Column
    private BigDecimal amount;
    @Column
    private int price;
    //falseにすることでEntity更新時に値が変更されない
    @Column(updatable = false)
    @CreatedDate
    private Date createdDate;
    @Column
    @LastModifiedDate
    private Date updatedDate;
}