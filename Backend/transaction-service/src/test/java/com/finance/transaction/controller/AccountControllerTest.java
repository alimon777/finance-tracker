package com.finance.transaction.controller;

import com.finance.transaction.dto.CustomResponse;
import com.finance.transaction.exceptions.ResourceNotFoundException;
import com.finance.transaction.model.Account;
import com.finance.transaction.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private Account sampleAccount;

    @BeforeEach
    void setUp() {
        sampleAccount = new Account(1L, 1L, "Sample Bank", "123456", 1000.0, Collections.emptyList());
    }

    @Test
    void getAccountsByUserId_ShouldReturnAccounts_WhenAccountsExist() throws Exception {
        CustomResponse<List<Account>> response = new CustomResponse<>("Success", List.of(sampleAccount));
        when(accountService.getAccountsByUserId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/transactions/accounts/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(sampleAccount.getId()))
                .andExpect(jsonPath("$.data[0].bankName").value(sampleAccount.getBankName()));
    }

    @Test
    void getAccountsByUserId_ShouldReturnNotFound_WhenNoAccountsExist() throws Exception {
        CustomResponse<List<Account>> response = new CustomResponse<>("No accounts found", null);
        when(accountService.getAccountsByUserId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/transactions/accounts/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void addAccount_ShouldReturnCreatedAccount() throws Exception {
        CustomResponse<Account> response = new CustomResponse<>("Account created", sampleAccount);
        when(accountService.addAccount(any(Account.class))).thenReturn(response);

        mockMvc.perform(post("/api/transactions/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"userId\":1,\"bankName\":\"Sample Bank\",\"accountNumber\":\"123456\",\"accountBalance\":1000.0}")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(sampleAccount.getId()))
                .andExpect(jsonPath("$.data.bankName").value(sampleAccount.getBankName()));
    }


    @Test
    void deleteAccount_ShouldReturnNoContent_WhenAccountExists() throws Exception {
        CustomResponse<Account> response = new CustomResponse<>("Account deleted", sampleAccount);
        when(accountService.deleteAccount(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/transactions/accounts/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteAccount_ShouldReturnNotFound_WhenAccountDoesNotExist() throws Exception {
        CustomResponse<Account> response = new CustomResponse<>("Account not found", null);
        when(accountService.deleteAccount(1L)).thenReturn(response);

        mockMvc.perform(delete("/api/transactions/accounts/1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
