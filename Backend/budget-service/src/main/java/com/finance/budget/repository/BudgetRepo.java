package com.finance.budget.repository;

import com.finance.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long>{
    List<Budget> findByUserId(Long userId);
}
