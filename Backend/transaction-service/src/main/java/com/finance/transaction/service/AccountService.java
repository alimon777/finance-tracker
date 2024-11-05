package com.finance.transaction.service;

import com.finance.transaction.model.Account;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public CustomResponse<List<Account>> getAccountsByUserId(Long userId) {
        List<Account> accounts = accountRepository.findAllByUserId(userId);
        if (accounts.isEmpty()) {
            return new CustomResponse<>("No accounts found for userId: " + userId, null);
        }
        return new CustomResponse<>("Accounts retrieved successfully", accounts);
    }

    public CustomResponse<Account> addAccount(Account account) {
        Account createdAccount = accountRepository.save(account);
        return new CustomResponse<>("Account created successfully", createdAccount);
    }

    public CustomResponse<Account> updateAccount(Long accountId, Account account) {
        Optional<Account> existingAccountOpt = accountRepository.findById(accountId);
        if (existingAccountOpt.isEmpty()) {
            return new CustomResponse<>("Account not found for accountId: " + accountId, null);
        }

        Account existingAccount = existingAccountOpt.get();
        existingAccount.setAccountNumber(account.getAccountNumber());
        existingAccount.setAccountBalance(account.getAccountBalance());
        // Update other fields as needed

        Account updatedAccount = accountRepository.save(existingAccount);
        return new CustomResponse<>("Account updated successfully", updatedAccount);
    }

    public CustomResponse<Account> deleteAccount(Long accountId) {
        // Check if the account exists
        if (!accountRepository.existsById(accountId)) {
            return new CustomResponse<>("Account not found for accountId: " + accountId, null);
        }

        // Fetch the account to be deleted
        Account accountToDelete = accountRepository.findById(accountId).orElse(null);

        // Delete the account
        accountRepository.deleteById(accountId);

        // Return the deleted account details in the response
        return new CustomResponse<>("Account deleted successfully", accountToDelete);
    }

}
