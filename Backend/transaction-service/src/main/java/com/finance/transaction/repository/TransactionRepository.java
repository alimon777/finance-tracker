package com.finance.transaction.repository;

import java.util.List;

import com.finance.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;




public interface TransactionRepository extends JpaRepository<Transaction, Long> {
	
	List<Transaction> findAllByUserId(Long userId);
}
