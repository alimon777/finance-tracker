package com.finance.budget.controller;

import com.finance.budget.model.Budget;
import com.finance.budget.service.BudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET,RequestMethod.DELETE, RequestMethod.POST, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/api/budgets")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    // Create a new budget
    @PostMapping
    public ResponseEntity<Budget> createBudget(@RequestBody Budget budget) {
        Budget createdBudget = budgetService.createBudget(budget);
        return new ResponseEntity<>(createdBudget, HttpStatus.CREATED);
    }

    // Get all budgets for a specific user
    @GetMapping("/{userId}")
    public ResponseEntity<List<Budget>> getBudgetsByUserId(@PathVariable Long userId) {
        List<Budget> budgets = budgetService.getBudgetsByUserId(userId);
        return ResponseEntity.ok(budgets);
    }

    // Get budget by ID
    @GetMapping("/{userId}/{id}")
    public ResponseEntity<Budget> getBudgetById(@PathVariable Long userId, @PathVariable Long id) {
        Optional<Budget> budget = budgetService.getBudgetById(id);
        return budget.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update budget
    @PutMapping("/{userId}/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable Long userId, @PathVariable Long id, @RequestBody Budget budgetDetails) {
        Budget updatedBudget = budgetService.updateBudget(id, budgetDetails);
        return ResponseEntity.ok(updatedBudget);
    }

    // Delete budget
    @DeleteMapping("/{userId}/{id}")
    public ResponseEntity<Void> deleteBudget(@PathVariable Long userId, @PathVariable Long id) {
        budgetService.deleteBudget(id);
        return ResponseEntity.noContent().build();
    }
}
