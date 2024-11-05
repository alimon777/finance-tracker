package com.finance.transaction.repository;

import com.finance.transaction.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account,Long> {
	List<Account> findAllByUserId(Long userId);
	Account findByAccountNumber(String accountNumber);
}
