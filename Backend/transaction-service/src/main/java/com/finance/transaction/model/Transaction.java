package com.finance.transaction.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity

@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Amount")
    private Double amount;

    private Long userId;

    @Column(name = "Description")
    private String description;
    
    @Column(name = "AccountNumber")
    private String accountNumber;

    @Column(name = "TransactionDate")
    @Temporal(TemporalType.DATE)
    private LocalDate transactionDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "TransactionType")
    private TransactionType transactionType;

    @Enumerated(EnumType.STRING)
    @Column(name = "CategoryType")
    private CategoryType categoryType;

    @ManyToOne
    @JsonIgnore
    private Account account;
}
