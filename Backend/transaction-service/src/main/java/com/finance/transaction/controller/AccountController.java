package com.finance.transaction.controller;

import com.finance.transaction.model.Account;
import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/test/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<CustomResponse<List<Account>>> getAccountsByUserId(@PathVariable Long userId) {
        CustomResponse<List<Account>> response = accountService.getAccountsByUserId(userId);
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Account>> addAccount(@RequestBody Account account) {
        CustomResponse<Account> response = accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/{accountId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CustomResponse<Account>> updateAccount(@PathVariable Long accountId, @RequestBody Account account) {
        CustomResponse<Account> response = accountService.updateAccount(accountId, account);
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<CustomResponse<Account>> deleteAccount(@PathVariable Long accountId) {
        CustomResponse<Account> response = accountService.deleteAccount(accountId);
        if (response.getData() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.noContent().build();
    }
}
