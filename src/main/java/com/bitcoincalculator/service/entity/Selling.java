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
@EntityListeners(AuditingEntityListener.class)
@Table(name="selling_amount")
@Getter
@Setter
public class Selling {
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
    @Column(updatable = false)
    @CreatedDate
    private Date createdDate;
    @Column
    @LastModifiedDate
    private Date updatedDate;
}