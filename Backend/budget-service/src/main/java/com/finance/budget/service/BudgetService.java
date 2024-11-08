package com.finance.budget.service;

import com.finance.budget.model.Budget;
import com.finance.budget.repository.BudgetRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepo budgetRepository;

    // Create a new budget
    public Budget createBudget(Budget budget) {
        return budgetRepository.save(budget);
    }

    // Get all budgets
    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    // Get all budgets for a specific user
    public List<Budget> getBudgetsByUserId(Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    // Get budget by ID
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    // Update budget
    public Budget updateBudget(Long id, Budget budgetDetails) {
        Budget budget = budgetRepository.findById(id).orElseThrow(() -> new RuntimeException("Budget not found"));
        budget.setUserId(budgetDetails.getUserId());
        budget.setBudgetStartDate(budgetDetails.getBudgetStartDate());
        budget.setBudgetEndDate(budgetDetails.getBudgetEndDate());
        budget.setFood(budgetDetails.getFood());
        budget.setHousing(budgetDetails.getHousing());
        budget.setTransportation(budgetDetails.getTransportation());
        budget.setEntertainment(budgetDetails.getEntertainment());
        budget.setIsExceeded(budgetDetails.getIsExceeded());
        return budgetRepository.save(budget);
    }

    // Delete budget
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
