package com.finance.budget.controller;

import com.finance.budget.model.Budget;
import com.finance.budget.service.BudgetService;
import com.finance.budget.dto.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        Budget createdBudget = budgetService.createBudget(budget);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Budget>> getBudgetsByUserId(@RequestParam Long userId) {
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        return ResponseEntity.ok(budgets);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/check-exceedance")
    ResponseEntity<String> checkExceedance(@RequestBody Transaction transaction){
    	return ResponseEntity.ok(budgetService.checkExceedance(transaction));
    }
}
