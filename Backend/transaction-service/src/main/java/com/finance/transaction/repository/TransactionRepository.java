package com.finance.transaction.repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.finance.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

	List<Transaction> findByUserIdAndTransactionDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
	List<Transaction> findAllByUserId(Long userId);
}
