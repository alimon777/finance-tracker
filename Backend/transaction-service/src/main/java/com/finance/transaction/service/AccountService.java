package com.finance.transaction.service;

import com.finance.transaction.model.Account;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.exceptions.AccountCreationException;
import com.finance.transaction.exceptions.DuplicateAccountNumberException;
import com.finance.transaction.exceptions.ResourceNotFoundException;
import com.finance.transaction.model.CategoryType;
import com.finance.transaction.model.Transaction;
import com.finance.transaction.model.TransactionType;
import com.finance.transaction.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    public CustomResponse<List<Account>> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        if (accounts.isEmpty()) {
            throw new ResourceNotFoundException("No accounts found for userId: " + userId);
        }
        return new CustomResponse<>("Accounts retrieved successfully", accounts);
    }

    public CustomResponse<Account> addAccount(Account account) {
        if (accountRepository.existsByAccountNumber(account.getAccountNumber())) {
            throw new DuplicateAccountNumberException("An account with the account number " + account.getAccountNumber() + " already exists.");
        }
        else {
        try {
            if (account.getAccountBalance() == null) {
                account.setAccountBalance(0.0);
            }
            Account createdAccount = accountRepository.save(account);
            createDummyTransactions(createdAccount);
            return new CustomResponse<>("Account created successfully", createdAccount);
        } catch (Exception e) {
            throw new AccountCreationException("Failed to create account: " + e.getMessage());
        }
        }
    }

    private void createDummyTransactions(Account account) {
    
         transactionService.addDummyTransaction(new Transaction(null, 150.0, account.getUserId(), "Salary deposit", account.getAccountNumber(), getDate("2024-11-06"), TransactionType.DEPOSIT, CategoryType.INCOME, account));
         transactionService.addDummyTransaction(new Transaction(null, 30.0, account.getUserId(), "Grocery shopping", account.getAccountNumber(), getDate("2024-11-05"), TransactionType.WITHDRAW, CategoryType.FOOD, account));
         transactionService.addDummyTransaction(new Transaction(null, 15.0, account.getUserId(), "Bus fare", account.getAccountNumber(), getDate("2024-11-04"), TransactionType.WITHDRAW, CategoryType.TRANSPORTATION, account));
         transactionService.addDummyTransaction(new Transaction(null, 100.0, account.getUserId(), "Rent payment", account.getAccountNumber(), getDate("2024-11-03"), TransactionType.WITHDRAW, CategoryType.HOUSING, account));
         transactionService.addDummyTransaction(new Transaction(null, 200.0, account.getUserId(), "Bonus received", account.getAccountNumber(), getDate("2024-11-02"), TransactionType.DEPOSIT, CategoryType.INCOME, account));
         transactionService.addDummyTransaction(new Transaction(null, 50.0, account.getUserId(), "Movie night", account.getAccountNumber(), getDate("2024-11-01"), TransactionType.WITHDRAW, CategoryType.ENTERTAINMENT, account));
         transactionService.addDummyTransaction(new Transaction(null, 250.0, account.getUserId(), "Salary deposit", account.getAccountNumber(), getDate("2024-10-06"), TransactionType.DEPOSIT, CategoryType.INCOME, account));
         transactionService.addDummyTransaction(new Transaction(null, 50.0, account.getUserId(), "Grocery shopping", account.getAccountNumber(), getDate("2024-10-05"), TransactionType.WITHDRAW, CategoryType.FOOD, account));
         transactionService.addDummyTransaction(new Transaction(null, 20.0, account.getUserId(), "Bus fare", account.getAccountNumber(), getDate("2024-10-04"), TransactionType.WITHDRAW, CategoryType.TRANSPORTATION, account));
         transactionService.addDummyTransaction(new Transaction(null, 400.0, account.getUserId(), "Rent payment", account.getAccountNumber(), getDate("2024-10-03"), TransactionType.WITHDRAW, CategoryType.HOUSING, account));
         transactionService.addDummyTransaction(new Transaction(null, 100.0, account.getUserId(), "Concert ticket", account.getAccountNumber(), getDate("2024-10-20"), TransactionType.WITHDRAW, CategoryType.ENTERTAINMENT, account));
         transactionService.addDummyTransaction(new Transaction(null, 150.0, account.getUserId(), "Bonus received", account.getAccountNumber(), getDate("2024-09-02"), TransactionType.DEPOSIT, CategoryType.INCOME, account));
         transactionService.addDummyTransaction(new Transaction(null, 30.0, account.getUserId(), "Video game purchase", account.getAccountNumber(), getDate("2024-09-15"), TransactionType.WITHDRAW, CategoryType.ENTERTAINMENT, account));
         transactionService.addDummyTransaction(new Transaction(null, 100.0, account.getUserId(), "Salary deposit", account.getAccountNumber(), getDate("2024-08-10"), TransactionType.DEPOSIT, CategoryType.INCOME, account));
         transactionService.addDummyTransaction(new Transaction(null, 40.0, account.getUserId(), "Grocery shopping", account.getAccountNumber(), getDate("2024-08-05"), TransactionType.WITHDRAW, CategoryType.FOOD, account));
         transactionService.addDummyTransaction(new Transaction(null, 25.0, account.getUserId(), "Bus fare", account.getAccountNumber(), getDate("2024-08-04"), TransactionType.WITHDRAW, CategoryType.TRANSPORTATION, account));
         transactionService.addDummyTransaction(new Transaction(null, 200.0, account.getUserId(), "Rent payment", account.getAccountNumber(), getDate("2024-08-03"), TransactionType.WITHDRAW, CategoryType.HOUSING, account));
         transactionService.addDummyTransaction(new Transaction(null, 50.0, account.getUserId(), "Salary deposit", account.getAccountNumber(), getDate("2024-11-11"), TransactionType.DEPOSIT, CategoryType.INCOME, account));
         transactionService.addDummyTransaction(new Transaction(null, 20.0, account.getUserId(), "Grocery shopping", account.getAccountNumber(), getDate("2024-11-12"), TransactionType.WITHDRAW, CategoryType.FOOD, account));
         transactionService.addDummyTransaction(new Transaction(null, 30.0, account.getUserId(), "Bus fare", account.getAccountNumber(), getDate("2024-11-13"), TransactionType.WITHDRAW, CategoryType.TRANSPORTATION, account));
         transactionService.addDummyTransaction(new Transaction(null, 100.0, account.getUserId(), "Rent payment", account.getAccountNumber(), getDate("2024-11-14"), TransactionType.WITHDRAW, CategoryType.HOUSING, account));

    }

    // Helper method to convert string date to Date object
    private Date getDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CustomResponse<Account> deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            return new CustomResponse<>("Account not found for accountId: " + accountId, null);
        }

        Account accountToDelete = accountRepository.findById(accountId).orElse(null);

        accountRepository.deleteById(accountId);

        return new CustomResponse<>("Account deleted successfully", accountToDelete);
    }

}
