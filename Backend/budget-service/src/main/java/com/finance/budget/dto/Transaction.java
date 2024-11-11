package com.finance.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    private Long id;
    private Double amount;

    private Long userId;
    private String description;
    private String accountNumber;
    private Date transactionDate;
    private TransactionType transactionType;
    private CategoryType categoryType;
}
