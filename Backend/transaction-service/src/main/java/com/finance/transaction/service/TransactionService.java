package com.finance.transaction.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.finance.transaction.model.Account;
import com.finance.transaction.client.BudgetServiceClient;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.AccountRepository;
import com.finance.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;



@Service
public class TransactionService {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BudgetServiceClient budgetServiceClient;

	public List<Transaction> getTransactions(Long userId) {
		return transactionRepository.findAllByUserId(userId);
	}

	public ResponseEntity<CustomResponse<Transaction>> addTransaction(Transaction transaction) {

		Account account = accountRepository.findByAccountNumber(transaction.getAccountNumber());
		if (account == null) {
			return new ResponseEntity<>(new CustomResponse<>("Account not found with account number: " + transaction.getAccountNumber(), null), HttpStatus.NOT_FOUND);
		}

		if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
			if (account.getAccountBalance() < transaction.getAmount()) {
				return new ResponseEntity<>(new CustomResponse<>("Insufficient funds for withdrawal.", null), HttpStatus.BAD_REQUEST);
			}
			account.setAccountBalance(account.getAccountBalance() - transaction.getAmount());
		} else {
			account.setAccountBalance(account.getAccountBalance() + transaction.getAmount());
		}
//
//		transaction.setBalance(account.getAccountBalance());
		transaction.setTransactionDate(new Date());
		transaction.setAccount(account);

		accountRepository.saveAndFlush(account);
		Transaction savedTransaction = transactionRepository.save(transaction);
		budgetServiceClient.checkExceedance(savedTransaction);
		return ResponseEntity.ok(new CustomResponse<>("Transaction successful", savedTransaction));
	}

	public void addTransactions(List<Transaction> transactions) {
		List<Transaction> savedTransactions = transactionRepository.saveAll(transactions);
		// Create a response with the list of saved transactions
	}
}
