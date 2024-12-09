package com.finance.transaction.controller;

import java.util.List;

import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.dto.ExpenditureSummaryDTO;
import com.finance.transaction.dto.IncomeDepositDTO;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.service.ExpenditureService;
import com.finance.transaction.service.IncomeDepositService;
import com.finance.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ExpenditureService expenditureService;
    
    @Autowired
    private IncomeDepositService incomeDepositService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Transaction>> addTransaction(@RequestBody Transaction transaction) {
    	CustomResponse<Transaction> response = transactionService.addTransaction(transaction);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PostMapping(value = "/multiple",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<String>> addMultipleTransactions(@RequestBody Transaction[] transactions) {
    	CustomResponse<String> response = transactionService.addMultipleTransactions(transactions);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactionsByUserId(@RequestParam Long userId) {
        List<Transaction> transactions = transactionService.getTransactions(userId);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/summary")
    public ResponseEntity<ExpenditureSummaryDTO> getExpenditureSummary(@RequestParam Long userId) {
        return ResponseEntity.ok(expenditureService.getExpenditureSummary(userId));
    }


    @GetMapping("/summary/weekly")
    public List<IncomeDepositDTO> getWeeklySummary(@RequestParam Long userId) {
        return incomeDepositService.getWeeklyDepositsAndWithdrawals(userId);
    }

    @GetMapping("/summary/monthly")
    public List<IncomeDepositDTO> getMonthlySummary(@RequestParam Long userId) {
        return incomeDepositService.getMonthlyDepositsAndWithdrawals(userId);
    }

    @GetMapping("/summary/yearly")
    public List<IncomeDepositDTO> getYearlySummary(@RequestParam Long userId) {
        return incomeDepositService.getYearlyDepositsAndWithdrawals(userId);
    }
    
}
