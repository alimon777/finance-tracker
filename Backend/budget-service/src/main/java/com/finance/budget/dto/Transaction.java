package com.finance.budget.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Transaction {

    private Long id;
    private Double amount;

    private Long userId;
    private String description;
    private String accountNumber;
    private LocalDate transactionDate;
    private TransactionType transactionType;
    private CategoryType categoryType;
    
    @Override
    public String toString() {
        return "a " + transactionType.toString().toLowerCase() + 
               " of amount " + amount +
               " from account number '" + accountNumber + 
               "' for the purpose of '" + description + 
               "' in the " + categoryType.toString().toLowerCase() + 
               " category on "+ transactionDate.format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy"));
    }
}
