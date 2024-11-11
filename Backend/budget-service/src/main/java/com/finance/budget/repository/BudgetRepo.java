package com.finance.budget.repository;

import com.finance.budget.model.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, Long>{
    List<Budget> findByUserId(Long userId);
    
    @Query("SELECT b FROM Budget b WHERE b.budgetStartDate <= :transactionDate AND b.budgetEndDate >= :transactionDate AND b.total >= 0 AND b.userId = :userId")
    List<Budget> findBudgetsByDateRangeAndNonNegativeTotalForUser(@Param("transactionDate") LocalDate transactionDate, @Param("userId") Long userId);
}
