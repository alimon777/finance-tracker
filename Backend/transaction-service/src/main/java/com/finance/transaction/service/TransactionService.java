package com.finance.transaction.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.finance.transaction.model.Account;
import com.finance.transaction.client.BudgetServiceClient;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.exceptions.InsufficientFundsException;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.AccountRepository;
import com.finance.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public CustomResponse<Transaction> addDummyTransaction(Transaction transaction) {
        CustomResponse<Transaction> response = processTransaction(transaction);
        return response;
    }

    public CustomResponse<Transaction> addTransaction(Transaction transaction) {
        CustomResponse<Transaction> response = processTransaction(transaction);
        if ("Transaction successful".equals(response.getMessage()) && transaction.getTransactionType() == TransactionType.WITHDRAW) {
        	CompletableFuture.runAsync(() -> {
                try {
                	budgetServiceClient.checkExceedance(response.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
        return response;
    }
    
    
    private CustomResponse<Transaction> processTransaction(Transaction transaction) {
        Account account = accountRepository.findByAccountNumber(transaction.getAccountNumber());
        if (account == null) {
            return new CustomResponse<>("No account found for the transaction: " + transaction.getAccountNumber(), null);
        }

        if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
            if (account.getAccountBalance() < transaction.getAmount()) {
                throw new InsufficientFundsException("Insufficient funds for withdrawal. Account balance: " 
                        + account.getAccountBalance() + ", requested withdrawal: " + transaction.getAmount());
            }
            account.setAccountBalance(account.getAccountBalance() - transaction.getAmount());
        } else {
            account.setAccountBalance(account.getAccountBalance() + transaction.getAmount());
        }

        if (transaction.getTransactionDate() == null) {
            transaction.setTransactionDate(LocalDate.now());
        }
        transaction.setAccount(account);

        accountRepository.saveAndFlush(account);
        Transaction savedTransaction = transactionRepository.save(transaction);

        return new CustomResponse<>("Transaction successful", savedTransaction);
    }
    
    public CustomResponse<String> addMultipleTransactions (Transaction[] transactions){
    	Account account = accountRepository.findByAccountNumber(transactions[0].getAccountNumber());
        if (account == null) {
            return new CustomResponse<>("No account found for the transaction: " + transactions[0].getAccountNumber(), null);
        }
        for (Transaction transaction:transactions) {
        	if (transaction.getTransactionType() == TransactionType.WITHDRAW) {
                if (account.getAccountBalance() < transaction.getAmount()) {
                    account.setAccountBalance(0d);
                }
                else if(account.getAccountBalance()!=0) {
                	account.setAccountBalance(account.getAccountBalance() - transaction.getAmount());
                }
            } else {
                account.setAccountBalance(account.getAccountBalance() + transaction.getAmount());
            }

            if (transaction.getTransactionDate() == null) {
                transaction.setTransactionDate(LocalDate.now());
            }
            transaction.setAccount(account);
            transactionRepository.save(transaction);
        }
        accountRepository.saveAndFlush(account);
        return new CustomResponse<>(transactions.length +" transactions were addded",null);
    }
}
