package com.finance.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finance.security.model.Budget;

import java.util.List;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long>{
    List<Budget> findByUserId(Long userId);
}
